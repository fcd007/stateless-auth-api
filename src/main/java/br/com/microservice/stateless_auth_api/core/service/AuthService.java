package br.com.microservice.stateless_auth_api.core.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import br.com.microservice.stateless_auth_api.core.repository.UserRepository;
import br.com.microservice.stateless_auth_api.domain.dto.AuthRequest;
import br.com.microservice.stateless_auth_api.domain.dto.TokenDTO;
import br.com.microservice.stateless_auth_api.infra.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public TokenDTO login(AuthRequest request) {
    var user = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new ValidationException("User not found"));

    var accessToken = jwtService.createToken(user);
    validatePassword(request.password(), user.getPassword());
    return new TokenDTO(accessToken);
  }

  private void validatePassword(String rawPassword, String encodedPassword) {
    if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
      throw new ValidationException("Invalid password");
    }
  }

  public TokenDTO validateToken(String accessToken) {
    validateExistingToken(accessToken);
    jwtService.validateAccessToken(accessToken);
    return new TokenDTO(accessToken);
  }

  private void validateExistingToken(String accessToken) {
    if (isEmpty(accessToken)) {
      throw new ValidationException("The access token not must be informed");
    }
  }
}
