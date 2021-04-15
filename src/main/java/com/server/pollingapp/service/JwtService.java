package com.server.pollingapp.service;

import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.request.RegistrationRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

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
                .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,securityKey)
                .compact();

    }
    private String CreateActivationToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 600000))
                .signWith(SignatureAlgorithm.HS256,securityKey)
                .compact();

    }
    private Claims ExtractAllClaims(String token){
        return Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody();

    }
    private <R> R ExtractSpecificClaim(String token, Function<Claims,R> claimsResolver){
       Claims allClaims= ExtractAllClaims(token);
       return claimsResolver.apply(allClaims);

    }
    private Date ExpiryDate(String token){
         return ExtractSpecificClaim(token,Claims::getExpiration);
    }

    private String ExtractUserName(String token){
        return ExtractSpecificClaim(token,Claims::getSubject);
    }

    private Boolean IsTokenNotExpired(String token){
        return ExpiryDate(token).after(new Date());
    }

    private Boolean IsAuthHeaderValid(String token, UserDetails pollsUserDetails){
        String username=ExtractUserName(token);
        return username.equals(pollsUserDetails.getUsername()) && IsTokenNotExpired(token);

    }
    public String GenerateLoginToken(UserModel user){
        Map<String,Object> payload = new HashMap<>();
        payload.put("Role",user.getRoles().toString());
        payload.put("Email",user.getEmail());
        payload.put("CreatedAt",user.getCreatedAt().toString());
        return CreateJwtToken(payload,user.getUsername());
    }

    public String GenerateAccountActivationToken(String username){

        return CreateActivationToken(username);


    }

}
