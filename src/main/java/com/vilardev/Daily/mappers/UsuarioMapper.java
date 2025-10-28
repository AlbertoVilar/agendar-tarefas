package com.vilardev.Daily.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {

    // Instancia do Mapper para ser usada quando necessário
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);
}
