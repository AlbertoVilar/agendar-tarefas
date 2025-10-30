package com.vilardev.Daily.services;

import com.vilardev.Daily.dtos.UsuarioRequestDTO;
import com.vilardev.Daily.dtos.UsuarioResponseDTO;
import com.vilardev.Daily.dtos.UsuarioUpdateDTO;
import com.vilardev.Daily.infrastructury.entities.Usuario;
import com.vilardev.Daily.infrastructury.entities.Role;
import com.vilardev.Daily.mappers.UsuarioMapper;
import com.vilardev.Daily.repositories.UsuarioRepository;
import com.vilardev.Daily.repositories.RoleRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public UsuarioResponseDTO createUser(UsuarioRequestDTO requestDTO) {
        // Validar e normalizar e-mail (trim + lowercase)
        if (requestDTO == null || requestDTO.email() == null || requestDTO.email().trim().isEmpty()) {
            throw new IllegalArgumentException("E-mail é obrigatório");
        }
        String normalizedEmail = requestDTO.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("E-mail já utilizado");
        }

        // Mapear DTO -> entidade
        Usuario usuario = usuarioMapper.toEntity(requestDTO);
        usuario.setEmail(normalizedEmail);

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

        // Roles: mapear por nome (se vierem) e garantir ROLE_USER
        Set<Role> rolesToAssign = new HashSet<>();
        if (requestDTO.roles() != null && !requestDTO.roles().isEmpty()) {
            for (String roleName : requestDTO.roles()) {
                if (roleName == null || roleName.isBlank()) continue;
                Role role = roleRepository.findByNomeRole(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role não encontrada: " + roleName));
                rolesToAssign.add(role);
            }
        }

        // Garante ROLE_USER sempre presente
        Role roleUser = roleRepository.findByNomeRole("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER não encontrada na base"));
        rolesToAssign.add(roleUser);

        usuario.setRoles(rolesToAssign);

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
        // TODO: implementar atualização por ID caso necessário
        throw new UnsupportedOperationException("updateUser não implementado");
    }

    @Transactional
    public UsuarioResponseDTO updateMe(UsuarioUpdateDTO dto) {
        // Obtém o e-mail do usuário autenticado a partir do SecurityContext (normalizado)
        String emailFromToken = getCurrentUserEmail();

        Usuario usuario = usuarioRepository.findByEmail(emailFromToken)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Atualização de e-mail (se enviado e mudou)
        if (dto.email() != null && !dto.email().isBlank()
                && !dto.email().equalsIgnoreCase(usuario.getEmail())) {
            String normalized = dto.email().trim().toLowerCase();

            if (usuarioRepository.existsByEmail(normalized)) {
                throw new IllegalArgumentException("E-mail já está em uso");
            }
            usuario.setEmail(normalized);
        }
        
        // Partial update via MapStruct (ignora null)
        usuarioMapper.updateEntityFromDto(dto, usuario);

        // Senha (se enviada)
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(salvo);
    }

    private String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Não autenticado");
        }
        String name = auth.getName();
        if (name == null || name.isBlank()) {
            throw new AccessDeniedException("Principal sem e-mail válido");
        }
        return name.trim().toLowerCase();
    }

    public void deleteUser(Long id) {
        // TODO: implementar remoção por ID
        throw new UnsupportedOperationException("deleteUser não implementado");
    }
}