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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

        //Verifi if user exists
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuarioId));

        //Convert DTO to Entity
        Endereco endereco = enderecoMapper.toEntity(requestDTO);

        //Associate to user
        endereco.setUsuario(usuario);

        //Save Address to DB
        Endereco saved = enderecoRespository.save(endereco);

        //Convert Entity to ResponseDTO end return it;
        return enderecoMapper.toResponseDTO(saved);
    }

    // LIST BY USER
    @Transactional(readOnly = true)
    public Page<EnderecoResponseDTO> listByUsuario(Long usuarioId, Pageable pageable) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        // valida existência do usuário para semântica clara
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuarioId));

        Page<Endereco> enderecos = enderecoRespository.findByUsuario_Id(usuarioId, pageable);
        return enderecos.map(enderecoMapper::toResponseDTO);
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public EnderecoResponseDTO getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do endereço não pode ser nulo");
        }
        Endereco endereco = enderecoRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com o id: " + id));
        return enderecoMapper.toResponseDTO(endereco);
    }

    // UPDATE ADDRESS
    @Transactional
    public EnderecoResponseDTO update(Long id, EnderecoRequestDTO requestDTO) {
        if (id == null || requestDTO == null)  {
            throw new IllegalArgumentException("ID do endereço e dados de requisição não podem ser nulos");
        }

        // Encontra o endereço ou lança uma exceção clara se não existir
        Endereco endereco = enderecoRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com o id: " + id));

        // Atualiza a entidade com os dados do DTO
        enderecoMapper.updateEntityFromDto(requestDTO, endereco);

        // Salva (opcional, mas claro) e retorna o DTO de resposta
        Endereco savedEndereco = enderecoRespository.save(endereco);
        return enderecoMapper.toResponseDTO(savedEndereco);
    }

    // DELETE ADDRESS
    @Transactional
    public EnderecoResponseDTO delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do endereço não pode ser nulo");
        }
        Endereco endereco = enderecoRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com o id: " + id));
        // Mapeia antes de deletar caso queira retornar o removido
        EnderecoResponseDTO dto = enderecoMapper.toResponseDTO(endereco);
        enderecoRespository.delete(endereco);
        return dto;
    }
}