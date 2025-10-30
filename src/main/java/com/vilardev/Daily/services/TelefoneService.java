package com.vilardev.Daily.services;

import com.vilardev.Daily.dtos.TelefoneRequestDTO;
import com.vilardev.Daily.dtos.TelefoneResponseDTO;
import com.vilardev.Daily.infrastructury.entities.Telefone;
import com.vilardev.Daily.infrastructury.entities.Usuario;
import com.vilardev.Daily.mappers.TelefoneMapper;
import com.vilardev.Daily.repositories.TelefoneRepository;
import com.vilardev.Daily.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TelefoneService {

    private final UsuarioRepository usuarioRepository;
    private final TelefoneRepository telefoneRepository;
    private final TelefoneMapper telefoneMapper;

    public TelefoneService(UsuarioRepository usuarioRepository,
                           TelefoneRepository telefoneRepository,
                           TelefoneMapper telefoneMapper) {
        this.usuarioRepository = usuarioRepository;
        this.telefoneRepository = telefoneRepository;
        this.telefoneMapper = telefoneMapper;
    }

    @Transactional
    public TelefoneResponseDTO addToUser(Long usuarioId, TelefoneRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuarioId));

        Telefone telefone = telefoneMapper.toEntity(requestDTO);
        telefone.setUsuario(usuario);

        Telefone saved = telefoneRepository.save(telefone);
        return telefoneMapper.toResponseDTO(saved);
    }

    // UPDATE TELEFONE
    @Transactional
    public TelefoneResponseDTO update(Long id, TelefoneRequestDTO requestDTO) {
        if (id == null || requestDTO == null) {
            throw new IllegalArgumentException("ID do telefone e dados de requisição não podem ser nulos");
        }

        Telefone telefone = telefoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado com o id: " + id));

        // Atualiza os campos do telefone com os dados do DTO
        telefoneMapper.updateEntityFromDto(requestDTO, telefone);

        Telefone savedTelefone = telefoneRepository.save(telefone);
        return telefoneMapper.toResponseDTO(savedTelefone);
    }

    // LIST BY USER
    @Transactional(readOnly = true)
    public java.util.List<TelefoneResponseDTO> listByUsuario(Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuarioId));

        java.util.List<Telefone> telefones = telefoneRepository.findByUsuario_Id(usuarioId);
        return telefones.stream()
                .map(telefoneMapper::toResponseDTO)
                .toList();
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public TelefoneResponseDTO getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do telefone não pode ser nulo");
        }
        Telefone telefone = telefoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado com o id: " + id));
        return telefoneMapper.toResponseDTO(telefone);
    }

    // DELETE TELEFONE
    @Transactional
    public TelefoneResponseDTO delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do telefone não pode ser nulo");
        }
        Telefone telefone = telefoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Telefone não encontrado com o id: " + id));
        TelefoneResponseDTO dto = telefoneMapper.toResponseDTO(telefone);
        telefoneRepository.delete(telefone);
        return dto;
    }
}