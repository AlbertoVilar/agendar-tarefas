package com.vilardev.Daily.controllers;

import com.vilardev.Daily.dtos.EnderecoRequestDTO;
import com.vilardev.Daily.dtos.EnderecoResponseDTO;
import com.vilardev.Daily.services.EnderecoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/usuarios/{usuarioId}/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> create(@PathVariable Long usuarioId,
                                                      @RequestBody EnderecoRequestDTO requestDTO) {
        EnderecoResponseDTO created = enderecoService.addToUser(usuarioId, requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/enderecos/" + (created != null ? created.id() : "")))
                .body(created);
    }

    // UPDATE ADDRESS
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> update(@PathVariable Long id,
                                                      @RequestBody EnderecoRequestDTO requestDTO) {
        EnderecoResponseDTO updated = enderecoService.update(id, requestDTO);
        return ResponseEntity.ok(updated);
    }
}