package com.vilardev.Daily.dtos;

public record TelefoneResponseDTO(
        Long id,
        String ddd,
        String numero,
        Long usuarioId
) {
}

