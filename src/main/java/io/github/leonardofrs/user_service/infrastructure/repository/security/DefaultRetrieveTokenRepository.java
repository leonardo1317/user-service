package io.github.leonardofrs.user_service.infrastructure.repository.security;

import io.github.leonardofrs.user_service.domain.dto.Token;
import io.github.leonardofrs.user_service.domain.repository.RetrieveTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class DefaultRetrieveTokenRepository implements RetrieveTokenRepository {

  private final String jwtSecret;
  private final long expirationTime;

  public DefaultRetrieveTokenRepository(
      @Value("${security.jwt.secret}") String jwtSecret,
      @Value("${security.jwt.expirationMillis}") long expirationTime) {
    this.jwtSecret = jwtSecret;
    this.expirationTime = expirationTime;
  }

  @Override
  public Token execute(String userId, String email) {
    Instant now = Instant.now();
    Instant expiry = now.plusMillis(expirationTime);

    String accessToken = Jwts.builder()
        .subject(userId)
        .claim("email", email)
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiry))
        .signWith(getSigningKey())
        .compact();

    return new Token(accessToken, now, expiry);
  }

  private Key getSigningKey() {
    byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
