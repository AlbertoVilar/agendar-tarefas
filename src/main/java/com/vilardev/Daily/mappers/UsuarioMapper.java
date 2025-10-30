package com.vilardev.Daily.mappers;

import com.vilardev.Daily.dtos.UsuarioRequestDTO;
import com.vilardev.Daily.dtos.UsuarioResponseDTO;
import com.vilardev.Daily.dtos.UsuarioUpdateDTO;
import com.vilardev.Daily.infrastructury.entities.Usuario;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.lang.annotation.Target;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {EnderecoMapper.class, TelefoneMapper.class}
)
public interface UsuarioMapper {

    //UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    // DTO -> Entidade (senha e roles são tratadas no service)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Usuario toEntity(UsuarioRequestDTO requestDTO);

    // Entidade -> DTO de resposta
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    // Atualização parcial específica para UsuarioUpdateDTO (ignora coleções e id)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enderecos", ignore = true)
    @Mapping(target = "telefones", ignore = true)
    void updateEntityFromDto(UsuarioUpdateDTO updateDTO, @MappingTarget Usuario entity);
}
