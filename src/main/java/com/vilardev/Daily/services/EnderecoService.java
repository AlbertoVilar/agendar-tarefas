package com.vilardev.Daily.services;

import com.vilardev.Daily.dtos.EnderecoRequestDTO;
import com.vilardev.Daily.dtos.EnderecoResponseDTO;
import com.vilardev.Daily.infrastructury.entities.Endereco;
import com.vilardev.Daily.mappers.EnderecoMapper;
import com.vilardev.Daily.repositories.EnderecoRespository;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    private final EnderecoRespository enderecoRespository;
    private final EnderecoMapper enderecoMapper;

    public EnderecoService(EnderecoRespository enderecoRespository, EnderecoMapper enderecoMapper) {
        this.enderecoRespository = enderecoRespository;
        this.enderecoMapper = enderecoMapper;
    }

    public EnderecoResponseDTO createNewAddress(EnderecoRequestDTO requestDTO) {
        // Lógica para criar um novo endereço
        Endereco endereco = enderecoRespository.save(
                enderecoMapper.toEntity(requestDTO));
        return enderecoMapper.toResponseDTO(endereco);
    }
}
