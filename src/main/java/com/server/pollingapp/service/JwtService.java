package com.server.pollingapp.service;

import com.server.pollingapp.models.UserModel;
import com.server.pollingapp.security.PollsUserDetails;
import io.jsonwebtoken.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${secret.key}") private String securityKey;

  @PostConstruct
  protected void init() {
    securityKey = Base64.getEncoder().encodeToString(securityKey.getBytes());
  }

  Logger log = LoggerFactory.getLogger(JwtService.class);

  private String CreateJwtToken(Map<String, Object> payload, String email) {
    return Jwts.builder()
        .setClaims(payload)
        .setSubject(email)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(
            new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)))
        .signWith(SignatureAlgorithm.HS256, securityKey)
        .compact();
  }
  private String CreateActivationToken(String email) {
    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() +
                                TimeUnit.MINUTES.toMillis(10)))
        .signWith(SignatureAlgorithm.HS256, securityKey)
        .compact();
  }

  private Claims ExtractAllClaims(String token) {
    return Jwts.parser()
        .setSigningKey(securityKey)
        .parseClaimsJws(token)
        .getBody();
  }
  private <R> R ExtractSpecificClaim(String token,
                                     Function<Claims, R> claimsResolver) {
    Claims allClaims = ExtractAllClaims(token);
    return claimsResolver.apply(allClaims);
  }
  private Date ExpiryDate(String token) {
    return ExtractSpecificClaim(token, Claims::getExpiration);
  }

  public String ExtractEmail(String token) {
    return ExtractSpecificClaim(token, Claims::getSubject);
  }

  public Boolean IsTokenNotExpired(String token) {
    return ExpiryDate(token).after(new Date());
  }

  public Boolean IsAuthHeaderValid(String token, UserDetails pollsUserDetails) {
    String username = ExtractEmail(token);
    return username.equals(pollsUserDetails.getUsername()) &&
        IsTokenNotExpired(token);
  }
  public String GenerateLoginToken(UserModel user) {
    Map<String, Object> payload = new HashMap<>();
    payload.put("Role", user.getRoles().toString());
    payload.put("Email", user.getEmail());
    payload.put("UserName", user.getUsername());
    payload.put("Id", user.getId());
    payload.put("Avatar", user.getImageurl());
    payload.put("AuthProvider", user.getAuthProvider().toString());
    payload.put("CreatedAt", user.getCreatedAt().toString());
    return CreateJwtToken(payload, user.getEmail());
  }

  public String GenerateOauthToken(Authentication authentication) {
    PollsUserDetails userPrincipal =
        (PollsUserDetails)authentication.getPrincipal();
    Object[] roles = userPrincipal.getAuthorities().toArray();
    Map<String, Object> payload = new HashMap<>();
    payload.put("Role", roles[0].toString());
    payload.put("Email", userPrincipal.getUsername());
    payload.put("UserName", userPrincipal.getUName());
    payload.put("Id", userPrincipal.getId());
    payload.put("Avatar", userPrincipal.getAvatar());
    payload.put("AuthProvider", userPrincipal.getAuthProvider());
    payload.put("CreatedAt", userPrincipal.getCreatedAt());

    return CreateJwtToken(payload, userPrincipal.getUsername());
  }

  public String GenerateAccountActivationToken(String email) {
    return CreateActivationToken(email);
  }

  public boolean ValidateToken(String token) {
    try {
      Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token);
    } catch (JwtException ex) {
      log.error(ex.getLocalizedMessage());
      return false;
    }

    return true;
  }
}
