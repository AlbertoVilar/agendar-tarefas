package com.vilardev.Daily.mappers;

import com.vilardev.Daily.dtos.TelefoneRequestDTO;
import com.vilardev.Daily.dtos.TelefoneResponseDTO;
import com.vilardev.Daily.infrastructury.entities.Telefone;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TelefoneMapper {

    // Mapeamento de DTO para Entidade
    Telefone toEntity(TelefoneRequestDTO telefoneRequestDTO);

    // Telefone para TelefoneResponseDTO
    TelefoneResponseDTO toResponseDTO(Telefone telefone);

    // Telefone update
    void updateEntityFromDto(TelefoneRequestDTO requestDTO, @MappingTarget Telefone telefone);

}
