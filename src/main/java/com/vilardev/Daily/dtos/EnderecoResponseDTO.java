package com.vilardev.Daily.dtos;

public record EnderecoResponseDTO(
        Long id,
        String rua,
        Long numero,
        String complemento,
        String cidade,
        String estado,
        String cep,
        Long usuarioId
) {
}

