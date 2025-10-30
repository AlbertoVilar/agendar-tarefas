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

    // LIST ADDRESSES BY USER
    @GetMapping
    public ResponseEntity<java.util.List<EnderecoResponseDTO>> list(@PathVariable Long usuarioId) {
        java.util.List<EnderecoResponseDTO> list = enderecoService.listByUsuario(usuarioId);
        return ResponseEntity.ok(list);
    }

    // GET ADDRESS BY ID
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> getById(@PathVariable Long id) {
        EnderecoResponseDTO dto = enderecoService.getById(id);
        return ResponseEntity.ok(dto);
    }

    // UPDATE ADDRESS
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> update(@PathVariable Long id,
                                                      @RequestBody EnderecoRequestDTO requestDTO) {
        EnderecoResponseDTO updated = enderecoService.update(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    // DELETE ADDRESS BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enderecoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}