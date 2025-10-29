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
        var request = new LoginRequest("albertovilar1@gmail.com", "132747");
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
    void me_shouldReturnUsernameAndRoles_whenAuthenticated() {
        // Login first
        var request = new LoginRequest("albertovilar1@gmail.com", "132747");
        var loginResp = restTemplate.postForEntity(baseUrl() + "/auth/login", request, LoginResponse.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResp.getBody()).isNotNull();

        String token = loginResp.getBody().token();

        // Call /me with Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> meResponse = restTemplate.exchange(
                baseUrl() + "/me",
                HttpMethod.GET,
                httpEntity,
                Map.class
        );

        assertThat(meResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meResponse.getBody()).isNotNull();
        assertThat(meResponse.getBody().get("username")).isEqualTo("albertovilar1@gmail.com");

        Object rolesObj = meResponse.getBody().get("roles");
        assertThat(rolesObj).isInstanceOf(List.class);
        @SuppressWarnings("unchecked")
        List<String> roles = ((List<?>) rolesObj).stream().map(Object::toString).toList();
        assertThat(roles).contains("ROLE_USER", "ROLE_ADMIN");
    }
}