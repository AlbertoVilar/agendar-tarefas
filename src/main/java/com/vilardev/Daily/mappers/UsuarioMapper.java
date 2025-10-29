package com.vilardev.Daily.mappers;

import com.vilardev.Daily.dtos.UsuarioRequestDTO;
import com.vilardev.Daily.dtos.UsuarioResponseDTO;
import com.vilardev.Daily.infrastructury.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

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

    // Atualização parcial da entidade a partir do DTO (sem alterar id/senha/roles diretamente)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateEntityFromDto(UsuarioRequestDTO requestDTO, @MappingTarget Usuario usuario);
}
