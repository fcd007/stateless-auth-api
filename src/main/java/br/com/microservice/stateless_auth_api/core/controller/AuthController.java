package br.com.microservice.stateless_auth_api.core.controller;

import br.com.microservice.stateless_auth_api.core.service.AuthService;
import br.com.microservice.stateless_auth_api.domain.dto.AuthRequest;
import br.com.microservice.stateless_auth_api.domain.dto.TokenDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<TokenDTO> login(@RequestBody AuthRequest request) {
    var token = authService.login(request);
    return ResponseEntity.ok(token);
  }

  @PostMapping("/token/validate")
  public ResponseEntity<TokenDTO> login(@RequestHeader String accessToken) {
    var token = authService.validateToken(accessToken);
    return ResponseEntity.ok(token);
  }
}
