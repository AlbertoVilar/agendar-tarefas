package com.vilardev.Daily.dtos;

// DTO para atualização parcial do usuário (ignora Endereços e Telefones)
public record UsuarioUpdateDTO(
        String nome,
        String email,
        String senha
) {}