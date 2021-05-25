package com.server.pollingapp.security;

import com.server.pollingapp.service.JwtService;
import com.server.pollingapp.service.PollsUserDetailsService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Autowired JwtService jwtService;
  @Autowired PollsUserDetailsService pollsUserDetailsService;

  /**
   * Same contract as for {@code doFilter}, but guaranteed to be
   * just invoked once per request within a single request thread.See {@link
   * #shouldNotFilterAsyncDispatch()} for details.<p>Provides HttpServletRequest
   * and HttpServletResponse arguments instead of the default ServletRequest and
   * ServletResponse ones.
   *
   * @param request
   * @param response
   * @param filterChain
   * @throws javax.servlet.ServletException
   * @throws java.io.IOException
   */

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {

    // GET AUTH HEADER
    final String authHeader = request.getHeader("Authorization");
    String email = null;
    String jwtToken = null;

    // UPDATE VALUES OF TOKEN AND USERNAME
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      // ACQUIRE TOKEN
      jwtToken = authHeader.substring(7);
      // VALIDATE IT TO ACQUIRE USERNAME ,IF SOMETHING IS WRONG WITH TOKEN
      // USERNAME=NULL
      if (jwtService.ValidateToken(jwtToken)) {
        email = jwtService.ExtractEmail(jwtToken);
      }
    }
    // IF TOKEN EXISTS,VALIDATE AND PLACE USER IN SESSION
    if (email != null &&
        SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails =
          pollsUserDetailsService.loadUserByUsername(email);
      // VALIDATE TOKEN DETAILS AND SET AUTHORIZED IN SECURITY CONTEXT
      Boolean isAuthHeaderValid =
          jwtService.IsAuthHeaderValid(jwtToken, userDetails);
      if (Boolean.TRUE.equals(isAuthHeaderValid)) {
        UsernamePasswordAuthenticationToken passToAuthorizationServer =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        passToAuthorizationServer.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(
            passToAuthorizationServer);

        // GENERATE LOG
      }
    }
    filterChain.doFilter(request, response);
  }
}
