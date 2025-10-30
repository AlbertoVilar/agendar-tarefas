package com.vilardev.Daily.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthMeIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    record LoginRequest(String email, String senha) {}
    record LoginResponse(String token, String type) {}

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void login_shouldReturnBearerToken() {
        // Register a new user first
        var registerBody = Map.of(
                "nome", "Teste",
                "email", "teste_login@example.com",
                "senha", "123456"
        );
        ResponseEntity<Map> registerResp = restTemplate.postForEntity(
                baseUrl() + "/auth/register",
                registerBody,
                Map.class
        );
        assertThat(registerResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Then login
        var request = new LoginRequest("teste_login@example.com", "123456");
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                baseUrl() + "/auth/login",
                request,
                LoginResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().type()).isEqualTo("Bearer");
        assertThat(response.getBody().token()).isNotBlank();
    }

    @Test
    void me_shouldReturn403_whenUserHasNoAdminRole() {
        // Register a new user with default ROLE_USER
        var registerBody = Map.of(
                "nome", "Teste",
                "email", "teste_me@example.com",
                "senha", "123456"
        );
        var registerResp = restTemplate.postForEntity(baseUrl() + "/auth/register", registerBody, Map.class);
        assertThat(registerResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Login
        var request = new LoginRequest("teste_me@example.com", "123456");
        var loginResp = restTemplate.postForEntity(baseUrl() + "/auth/login", request, LoginResponse.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResp.getBody()).isNotNull();
        String token = loginResp.getBody().token();

        // Call /me (requires ADMIN) with Bearer token -> expect 403
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> meResponse = restTemplate.exchange(
                baseUrl() + "/me",
                HttpMethod.GET,
                httpEntity,
                Map.class
        );

        assertThat(meResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}