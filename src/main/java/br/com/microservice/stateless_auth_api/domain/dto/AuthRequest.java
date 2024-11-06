package br.com.microservice.stateless_auth_api.domain.dto;

public record AuthRequest(String username, String password) {
}
