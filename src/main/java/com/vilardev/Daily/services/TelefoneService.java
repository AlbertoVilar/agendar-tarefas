package com.vilardev.Daily.services;

import com.vilardev.Daily.dtos.TelefoneRequestDTO;
import com.vilardev.Daily.dtos.TelefoneResponseDTO;
import com.vilardev.Daily.infrastructury.entities.Telefone;
import com.vilardev.Daily.mappers.TelefoneMapper;
import com.vilardev.Daily.repositories.TelefoneRepository;
import org.springframework.stereotype.Service;

@Service
public class TelefoneService {

    private final TelefoneRepository telefoneRepository;
    private final TelefoneMapper telefoneMapper;

    public TelefoneService(TelefoneRepository telefoneRepository, TelefoneMapper telefoneMapper) {
        this.telefoneRepository = telefoneRepository;
        this.telefoneMapper = telefoneMapper;
    }

    public TelefoneResponseDTO creatTelefone(TelefoneRequestDTO requestDTO) {

        Telefone telefone = telefoneRepository.save(telefoneMapper.toEntity(requestDTO));
        return telefoneMapper.toResponseDTO(telefone);
    }
}
