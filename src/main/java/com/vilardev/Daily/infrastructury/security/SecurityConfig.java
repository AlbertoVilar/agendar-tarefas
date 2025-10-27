package com.vilardev.Daily.infrastructury.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                // enable HTTP Basic so clients can send Basic credentials
                .httpBasic();

        // Allow H2 console frames (ONLY for development/testing)
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        // Exemplo (comentado) de como registrar o filtro JWT que implementa a autenticação
        // baseada em TokenService (geração/validação) e UserDetailsService (carrega usuário).
        // Quando você tiver esses beans disponíveis, descomente e adapte conforme necessário:
        //
        // // obtenha os beans TokenService e UserDetailsService via injeção (por construtor da config)
        // // TokenService tokenService = ...;
        // // org.springframework.security.core.userdetails.UserDetailsService uds = ...;
        // // Crie o filtro e registre antes do UsernamePasswordAuthenticationFilter
        // // com o nome completo da classe para evitar imports desnecessários:
        // JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(tokenService, uds);
        // http.addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
