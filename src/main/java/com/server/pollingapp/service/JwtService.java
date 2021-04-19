package com.server.pollingapp.service;

import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.request.RealTimeLogRequest;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
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
        return  Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody();

    }
    private <R> R ExtractSpecificClaim(String token, Function<Claims,R> claimsResolver){
       Claims allClaims= ExtractAllClaims(token);
       return claimsResolver.apply(allClaims);

    }
    private Date ExpiryDate(String token){
         return ExtractSpecificClaim(token,Claims::getExpiration);
    }

    public String ExtractUserName(String token){
        return ExtractSpecificClaim(token,Claims::getSubject);
    }

    public Boolean IsTokenNotExpired(String token){
        return ExpiryDate(token).after(new Date());
    }

    public Boolean IsAuthHeaderValid(String token, UserDetails pollsUserDetails){
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

    public boolean ValidateToken(String token){
        try {
            Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token);
            return true;
        }
        catch (SignatureException ex) {
            pollStream.sendToMessageBroker(new RealTimeLogRequest("ERROR",ex.getMessage(),"JWtService"));
        } catch (MalformedJwtException ex) {
            pollStream.sendToMessageBroker(new RealTimeLogRequest("ERROR",ex.getMessage(),"JWtService"));
        } catch (ExpiredJwtException ex) {
            pollStream.sendToMessageBroker(new RealTimeLogRequest("ERROR",ex.getMessage(),"JWtService"));
        } catch (UnsupportedJwtException ex) {
            pollStream.sendToMessageBroker(new RealTimeLogRequest("ERROR",ex.getMessage(),"JWtService"));
        } catch (IllegalArgumentException ex) {
            pollStream.sendToMessageBroker(new RealTimeLogRequest("ERROR",ex.getMessage(),"JWtService"));
        }

        return false;
    }

}
