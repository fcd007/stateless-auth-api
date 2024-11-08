package br.com.microservice.stateless_auth_api.core.controller;

import br.com.microservice.stateless_auth_api.core.service.AuthService;
import br.com.microservice.stateless_auth_api.domain.dto.AuthRequest;
import br.com.microservice.stateless_auth_api.domain.dto.TokenDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<TokenDTO> login(@RequestBody AuthRequest request) {
    log.info("Request received to login");

    var token = authService.login(request);

    log.info("Logged in successfully");
    return ResponseEntity.ok(token);
  }

  @PostMapping("/token/validate-token")
  public ResponseEntity<TokenDTO> validateToken(@RequestHeader String accessToken) {
    log.info("Request received to validate token");

    var token = authService.validateToken(accessToken);

    log.info("Token validated successfully");
    return ResponseEntity.ok(token);
  }
}
