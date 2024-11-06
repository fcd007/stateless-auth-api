package br.com.microservice.stateless_auth_api.core.repository;

import br.com.microservice.stateless_auth_api.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsername(String username);
}
