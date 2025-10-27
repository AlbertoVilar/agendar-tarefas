package com.vilardev.Daily.controllers;

import com.vilardev.Daily.application.security.TokenService;
import com.vilardev.Daily.dtos.TokenResponseDTO;
import com.vilardev.Daily.dtos.UsuarioLoginDTO;
import com.vilardev.Daily.infrastructury.entities.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody UsuarioLoginDTO loginDTO) {
        var authToken = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha());
        Authentication authentication = authenticationManager.authenticate(authToken);
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = tokenService.generateToken(usuario);
        return ResponseEntity.ok(new TokenResponseDTO(token, "Bearer"));
    }
}
