package com.vilardev.Daily.mappers;


import com.vilardev.Daily.dtos.EnderecoRequestDTO;
import com.vilardev.Daily.dtos.EnderecoResponseDTO;
import com.vilardev.Daily.infrastructury.entities.Endereco;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnderecoMapper {

    EnderecoResponseDTO toResponseDTO(Endereco endereco);

    Endereco toEntity(EnderecoRequestDTO enderecoRequestDTO);

    @Mapping(target = "id", ignore = true) // Garante que o ID da entidade não seja sobrescrito (para null)
    void updateEntityFromDto(EnderecoRequestDTO requestDTO, @MappingTarget Endereco endereco);
}