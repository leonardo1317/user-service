package io.github.leonardofrs.user_service.infrastructure.repository.security;

import io.github.leonardofrs.user_service.domain.repository.RetrieveTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultRetrieveTokenRepository implements RetrieveTokenRepository {

  @Value("${app.security.jwt.secret}")
  private String jwtSecret;

  @Value("${app.security.jwt.expirationMillis}")
  private long expirationTime;

  @Override
  public String execute(String userId, String email) {
    Instant now = Instant.now();
    Date issuedAt = Date.from(now);
    Date expiryDate = Date.from(now.plusMillis(expirationTime));

    return Jwts.builder()
        .subject(userId)
        .claim("email", email)
        .issuedAt(issuedAt)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  private Key getSigningKey() {
    byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
