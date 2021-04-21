package com.server.pollingapp.service;

import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.request.RealTimeLogRequest;
import com.server.pollingapp.security.PollsUserDetails;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {
    @Autowired
    PollStream pollStream;

    @Value("${secret.key}")
    private String securityKey;

    @PostConstruct
    protected void init() {
        securityKey = Base64.getEncoder().encodeToString(securityKey.getBytes());
    }

    private String CreateJwtToken(Map<String,Object> payload, String username){
        return Jwts.builder()
                .setClaims(payload)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ TimeUnit.HOURS.toMillis(1)))
                .signWith(SignatureAlgorithm.HS256,securityKey)
                .compact();

    }
    private String CreateActivationToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(10)))
                .signWith(SignatureAlgorithm.HS256,securityKey)
                .compact();

    }

    private Claims ExtractAllClaims(String token){
        return  Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody();

    }
    private <R> R ExtractSpecificClaim(String token, Function<Claims,R> claimsResolver){
       Claims allClaims= ExtractAllClaims(token);
       return claimsResolver.apply(allClaims);

    }
    private Date ExpiryDate(String token){
         return ExtractSpecificClaim(token,Claims::getExpiration);
    }

    public String ExtractEmail(String token){
        return ExtractSpecificClaim(token,Claims::getSubject);
    }

    public Boolean IsTokenNotExpired(String token){
        return ExpiryDate(token).after(new Date());
    }

    public Boolean IsAuthHeaderValid(String token, UserDetails pollsUserDetails){
        String username= ExtractEmail(token);
        return username.equals(pollsUserDetails.getUsername()) && IsTokenNotExpired(token);

    }
    public String GenerateLoginToken(UserModel user){
        Map<String,Object> payload = new HashMap<>();
        payload.put("Role",user.getRoles().toString());
        payload.put("Email",user.getEmail());
        payload.put("Id",user.getId());
        payload.put("Avatar",user.getImageurl());
        payload.put("AuthProvider",user.getAuthProvider().toString());
        payload.put("CreatedAt",user.getCreatedAt().toString());
        return CreateJwtToken(payload,user.getUsername());
    }

    public String GenerateOauthToken(Authentication authentication){
        PollsUserDetails userPrincipal = (PollsUserDetails) authentication.getPrincipal();
        Map<String,Object> payload = new HashMap<>();
        payload.put("Role",userPrincipal.getAuthorities().toString());
        payload.put("Email",userPrincipal.getUsername());
        payload.put("Id",userPrincipal.getId());
        payload.put("Avatar",userPrincipal.getAvatar());
        payload.put("AuthProvider",userPrincipal.getAuthProvider());
        payload.put("CreatedAt",userPrincipal.getCreatedAt());


        return CreateJwtToken(payload,userPrincipal.getUName());
    }

    public String GenerateAccountActivationToken(String email){
        return CreateActivationToken(email);
    }

    public boolean ValidateToken(String token){
        try {
            Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token);
        }
        catch (JwtException ex) {
            pollStream.sendToMessageBroker(new RealTimeLogRequest("ERROR",ex.getMessage(),"JWtService"));
            return false;
        }

        return true;
    }

}
