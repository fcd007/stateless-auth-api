package br.com.microservice.stateless_auth_api.core.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import br.com.microservice.stateless_auth_api.domain.model.User;
import br.com.microservice.stateless_auth_api.infra.exception.AuthenticationException;
import br.com.microservice.stateless_auth_api.infra.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private static final Date DATE = new Date();
  private static final Integer EXPIRATION_TIME_HOUR = 2;
  private static final String EMPTY_STRING_SPACE = " ";
  private static final Integer TOKEN_INDEX = 1;

  @Value("${app.token.secret-key}")
  private String secretKey;

  public String createToken(User user) {
    var data = new HashMap<String, String>();
    data.put("id", String.valueOf(user.getId()));
    data.put("username", user.getUsername());

    return Jwts
        .builder().claims(data)
        .issuedAt(DATE)
        .expiration(generateExpireAt())
        .signWith(generateSign())
        .compact();
  }

  private Date generateExpireAt() {
    return Date.from(
        LocalDateTime
            .now()
            .plusHours(EXPIRATION_TIME_HOUR)
            .atZone(ZoneId.systemDefault()).toInstant()
    );
  }

  private SecretKey generateSign() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public void validateAccessToken(String token) {
    var accessToken = extractToken(token);
    try {
      Jwts
          .parser()
          .verifyWith(generateSign())
          .build()
          .parseSignedClaims(accessToken)
          .getPayload();
    } catch (Exception exception) {
      throw new AuthenticationException("Invalid access token" + exception.getMessage());
    }
  }

  private String extractToken(String token) {
    if (isEmpty(token)) {
      throw new ValidationException("The access Token is required");
    }

    if (!token.contains(EMPTY_STRING_SPACE)) {
      String[] tokenParts = token.split(EMPTY_STRING_SPACE);

      if (tokenParts.length > 1) {
        return tokenParts[TOKEN_INDEX];
      }
    }

    return token;
  }
}
