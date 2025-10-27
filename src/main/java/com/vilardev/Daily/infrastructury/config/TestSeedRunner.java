package com.vilardev.Daily.infrastructury.config;

import com.vilardev.Daily.infrastructury.entities.Usuario;
import com.vilardev.Daily.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestSeedRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TestSeedRunner.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public TestSeedRunner(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String email = "albertovilar1@gmail.com";
        var existingOpt = usuarioRepository.findByEmail(email);
        if (existingOpt.isPresent()) {
            var user = existingOpt.get();
            String current = user.getSenha();
            if (current == null || !passwordEncoder.matches("132747", current)) {
                user.setSenha(passwordEncoder.encode("132747"));
                usuarioRepository.save(user);
                log.info("[test] Updated seed user password to encoded for: {}", email);
            } else {
                log.info("[test] Seed user already exists with valid encoded password: {}", email);
            }
            return;
        }

        Usuario user = Usuario.builder()
                .nome("Alberto Vilar")
                .email(email)
                .senha(passwordEncoder.encode("132747"))
                .build();

        usuarioRepository.save(user);
        log.info("[test] Seed user created: {}", email);
    }
}