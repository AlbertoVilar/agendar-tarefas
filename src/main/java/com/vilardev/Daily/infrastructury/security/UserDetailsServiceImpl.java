package com.vilardev.Daily.infrastructury.security;

import com.vilardev.Daily.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)  {
        // Normaliza o e-mail antes de buscar
        String normalized = username == null ? null : username.trim().toLowerCase();
        if (normalized == null || normalized.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado: e-mail vazio");
        }
        return usuarioRepository.findByEmail(normalized)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com e-mail: " + normalized));
    }
}
