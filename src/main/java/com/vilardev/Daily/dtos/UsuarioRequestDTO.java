package com.vilardev.Daily.dtos;

import java.util.List;
import java.util.Set;

// Versão em português do DTO de requisição do usuário
public record UsuarioRequestDTO(
        String nome,
        String email,
        String senha,
        List<EnderecoRequestDTO> enderecos,
        List<TelefoneRequestDTO> telefones,
        Set<String> roles
) {
}

