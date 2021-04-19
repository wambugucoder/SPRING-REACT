package com.server.pollingapp.security;

import com.server.pollingapp.request.RealTimeLogRequest;
import com.server.pollingapp.service.JwtService;
import com.server.pollingapp.service.PollStream;
import com.server.pollingapp.service.PollsUserDetailsService;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    final JwtService jwtService;

    final PollStream pollStream;

    final PollsUserDetailsService pollsUserDetailsService;

    public JwtFilter(JwtService jwtService, PollStream pollStream, PollsUserDetailsService pollsUserDetailsService) {
        this.jwtService = jwtService;
        this.pollStream = pollStream;
        this.pollsUserDetailsService = pollsUserDetailsService;
    }

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    String username=null;
    String jwtToken=null;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

          //GET AUTH HEADER
       final String authHeader= request.getHeader("Authorization");
          //UPDATE VALUES OF TOKEN AND USERNAME
        if(authHeader!= null && authHeader.startsWith("Bearer ")){
            //ACQUIRE TOKEN
            jwtToken=authHeader.substring(7);
            //VALIDATE IT TO ACQUIRE USERNAME ,IF SOMETHING IS WRONG WITH TOKEN USERNAME=NULL
             if (jwtService.ValidateToken(jwtToken)){
                 username=jwtService.ExtractUserName(jwtToken);
             }
        }
        else{
            pollStream.sendToMessageBroker(new RealTimeLogRequest("ERROR","Token Does Not Begin with Bearer String","JWTFILTER"));
        }
        //IF TOKEN EXISTS,VALIDATE AND PLACE USER IN SESSION
        if (username!= null && SecurityContextHolder.getContext().getAuthentication()==null){
           UserDetails userDetails=pollsUserDetailsService.loadUserByUsername(username);
           //VALIDATE TOKEN DETAILS AND SET AUTHORIZED IN SECURITY CONTEXT
            if (jwtService.IsAuthHeaderValid(jwtToken, userDetails)){
                UsernamePasswordAuthenticationToken passToAuthorizationServer = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                passToAuthorizationServer.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(passToAuthorizationServer);

                //GENERATE LOG
                pollStream.sendToMessageBroker(new RealTimeLogRequest("INFO",username +"has been authorized to access"+request.getRequestURI(),"JwtFilter"));
            }

        }
        filterChain.doFilter(request,response);
    }
}
