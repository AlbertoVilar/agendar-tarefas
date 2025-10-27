package com.vilardev.Daily.dtos;

public record EnderecoRequestDTO(
        String rua,
        Long numero,
        String complemento,
        String cidade,
        String estado,
        String cep
) {
}

