package com.vilardev.Daily.dtos;

import com.vilardev.Daily.infrastructury.entities.Endereco;
import com.vilardev.Daily.infrastructury.entities.Telefone;
import com.vilardev.Daily.infrastructury.entities.Usuario;

import java.util.List;
import java.util.stream.Collectors;

public final class UsuarioMapper {

    private UsuarioMapper() {}

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null;
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());

        if (dto.enderecos() != null) {
            for (EnderecoRequestDTO eDto : dto.enderecos()) {
                if (eDto == null) continue;
                Endereco e = new Endereco();
                e.setRua(eDto.rua());
                e.setNumero(eDto.numero());
                e.setComplemento(eDto.complemento());
                e.setCidade(eDto.cidade());
                e.setEstado(eDto.estado());
                e.setCep(eDto.cep());
                usuario.addEndereco(e);
            }
        }

        if (dto.telefones() != null) {
            for (TelefoneRequestDTO tDto : dto.telefones()) {
                if (tDto == null) continue;
                Telefone t = new Telefone();
                t.setDdd(tDto.ddd());
                t.setNumero(tDto.numero());
                usuario.addTelefone(t);
            }
        }

        // roles are strings in DTO; entity expects Role objects. Mapping roles is out of scope here.
        return usuario;
    }

    public static UsuarioResponseDTO toResponse(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public static List<UsuarioResponseDTO> toResponseList(List<Usuario> usuarios) {
        if (usuarios == null) return List.of();
        return usuarios.stream().map(UsuarioMapper::toResponse).collect(Collectors.toList());
    }
}

