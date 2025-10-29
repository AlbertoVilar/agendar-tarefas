package com.vilardev.Daily.services;

import com.vilardev.Daily.dtos.UsuarioRequestDTO;
import com.vilardev.Daily.dtos.UsuarioResponseDTO;
import com.vilardev.Daily.infrastructury.entities.Usuario;
import com.vilardev.Daily.mappers.UsuarioMapper;
import com.vilardev.Daily.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO createUser(UsuarioRequestDTO requestDTO) {
        // Validar unicidade de e-mail
        if (requestDTO == null || requestDTO.email() == null || requestDTO.email().isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório");
        }
        if (usuarioRepository.existsByEmail(requestDTO.email())) {
            throw new IllegalArgumentException("E-mail já utilizado");
        }

        // Mapear DTO -> entidade
        Usuario usuario = usuarioMapper.toEntity(requestDTO);

        // Senha: encode (se fornecida)
        if (requestDTO.senha() != null && !requestDTO.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(requestDTO.senha()));
        }

        // Garantir relacionamento bidirecional com filhos
        if (usuario.getEnderecos() != null) {
            usuario.getEnderecos().forEach(e -> {
                if (e != null) e.setUsuario(usuario);
            });
        }
        if (usuario.getTelefones() != null) {
            usuario.getTelefones().forEach(t -> {
                if (t != null) t.setUsuario(usuario);
            });
        }

        // TODO: Mapear roles a partir de nomes (ex.: via RoleRepository)
        // usuario.setRoles(...);

        // Persistir
        Usuario saved = usuarioRepository.save(usuario);

        // Entidade -> DTO de resposta
        return usuarioMapper.toResponseDTO(saved);
    }

    public UsuarioResponseDTO getUserById(Long id) {
        // TODO: implementar busca por ID (buscar, tratar not found, mapear para response)
        throw new UnsupportedOperationException("getUserById não implementado");
    }

    public List<UsuarioResponseDTO> listUsers() {
        // TODO: implementar listagem (buscar todos e mapear para response)
        throw new UnsupportedOperationException("listUsers não implementado");
    }

    public UsuarioResponseDTO updateUser(Long id, UsuarioRequestDTO requestDTO) {
        // TODO: implementar atualização (buscar existente, aplicar mudanças, salvar, mapear)
        throw new UnsupportedOperationException("updateUser não implementado");
    }

    public void deleteUser(Long id) {
        // TODO: implementar remoção por ID
        throw new UnsupportedOperationException("deleteUser não implementado");
    }
}