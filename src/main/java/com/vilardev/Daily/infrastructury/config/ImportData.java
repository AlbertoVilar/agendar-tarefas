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
@Profile("!test") // evita rodar em testes automaticamente
public class ImportData implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ImportData.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ImportData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String email = "albertovilar1@gmail.com";
        var existing = usuarioRepository.findByEmail(email);
        if (existing.isPresent()) {
            log.info("Seed user already exists: {}", email);
            return;
        }

        Usuario user = Usuario.builder()
                .nome("Alberto Vilar")
                .email(email)
                .senha(passwordEncoder.encode("132747"))
                .build();

        usuarioRepository.save(user);
        log.info("Seed user created: {}", email);
    }
}

