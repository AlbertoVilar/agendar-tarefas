package com.vilardev.Daily.services;

import com.vilardev.Daily.dtos.UsuarioRequestDTO;
import com.vilardev.Daily.dtos.UsuarioResponseDTO;
import com.vilardev.Daily.mappers.UsuarioMapper;
import com.vilardev.Daily.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public UsuarioResponseDTO createUser(UsuarioRequestDTO requestDTO) {
        // TODO: implementar criação de usuário (mapear DTO -> entidade, encode de senha, salvar, mapear para response)
        throw new UnsupportedOperationException("createUser não implementado");
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