package com.vilardev.Daily.services;

import com.vilardev.Daily.dtos.EnderecoRequestDTO;
import com.vilardev.Daily.dtos.EnderecoResponseDTO;
import com.vilardev.Daily.infrastructury.entities.Endereco;
import com.vilardev.Daily.infrastructury.entities.Usuario;
import com.vilardev.Daily.mappers.EnderecoMapper;
import com.vilardev.Daily.repositories.EnderecoRespository;
import com.vilardev.Daily.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnderecoService {

    private final UsuarioRepository usuarioRepository;
    private final EnderecoRespository enderecoRespository;
    private final EnderecoMapper enderecoMapper;

    public EnderecoService(UsuarioRepository usuarioRepository,
                           EnderecoRespository enderecoRespository,
                           EnderecoMapper enderecoMapper) {
        this.usuarioRepository = usuarioRepository;
        this.enderecoRespository = enderecoRespository;
        this.enderecoMapper = enderecoMapper;
    }

    @Transactional
    public EnderecoResponseDTO addToUser(Long usuarioId, EnderecoRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuarioId));

        Endereco endereco = enderecoMapper.toEntity(requestDTO);
        endereco.setUsuario(usuario);

        Endereco saved = enderecoRespository.save(endereco);
        return enderecoMapper.toResponseDTO(saved);
    }
}