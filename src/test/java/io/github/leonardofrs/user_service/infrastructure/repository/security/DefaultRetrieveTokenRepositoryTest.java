package io.github.leonardofrs.user_service.infrastructure.repository.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.leonardofrs.user_service.domain.dto.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultRetrieveTokenRepositoryTest {

  private static final String SECRET = "BIXSbl2F8xuoUXx43MWNRAsgTcpkkKLTX4fMrKc2cXg=";
  private static final SecretKey KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET));
  private static final long EXPIRATION_TIME = 3600000L;

  @Test
  @DisplayName("should generate a valid JWT token containing subject and email claim")
  void shouldGenerateValidJwtTokenContainingSubjectAndEmailClaim() {
    String userId = "user-123";
    String email = "test@example.com";
    DefaultRetrieveTokenRepository repository =
        new DefaultRetrieveTokenRepository(SECRET, EXPIRATION_TIME);

    Token token = repository.execute(userId, email);

    assertNotNull(token);
    Claims claims = parseToken(token);
    assertEquals(userId, claims.getSubject());
    assertEquals(email, claims.get("email"));
  }

  @Test
  @DisplayName("should set issuedAt and expiration times correctly in token")
  void shouldSetIssuedAtAndExpirationTimesCorrectlyInToken() {
    String userId = "user-456";
    String email = "test@example.com";
    DefaultRetrieveTokenRepository repository =
        new DefaultRetrieveTokenRepository(SECRET, EXPIRATION_TIME);

    Token token = repository.execute(userId, email);

    Claims claims = parseToken(token);
    Date issuedAt = claims.getIssuedAt();
    Date expiration = claims.getExpiration();

    assertNotNull(issuedAt);
    assertNotNull(expiration);
    assertTrue(expiration.after(issuedAt));
    assertTrue(expiration.getTime() - issuedAt.getTime() <= EXPIRATION_TIME + 1000);
  }

  private Claims parseToken(Token token) {
    return Jwts.parser()
        .verifyWith(KEY)
        .build()
        .parseSignedClaims(token.value())
        .getPayload();
  }
}
