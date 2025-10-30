package com.vilardev.Daily.controllers;

import com.vilardev.Daily.dtos.TelefoneRequestDTO;
import com.vilardev.Daily.dtos.TelefoneResponseDTO;
import com.vilardev.Daily.services.TelefoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/usuarios/{usuarioId}/telefones")
public class TelefoneController {

    private final TelefoneService telefoneService;

    public TelefoneController(TelefoneService telefoneService) {
        this.telefoneService = telefoneService;
    }

    // LIST TELEFONES BY USER (paginated)
    @GetMapping
    public ResponseEntity<Page<TelefoneResponseDTO>> list(@PathVariable Long usuarioId,
                                                          @PageableDefault(size = 20) Pageable pageable) {
        Page<TelefoneResponseDTO> page = telefoneService.listByUsuario(usuarioId, pageable);
        return ResponseEntity.ok(page);
    }

    // GET TELEFONE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TelefoneResponseDTO> getById(@PathVariable Long id) {
        TelefoneResponseDTO dto = telefoneService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TelefoneResponseDTO> create(@PathVariable Long usuarioId,
                                                      @RequestBody TelefoneRequestDTO requestDTO) {
        TelefoneResponseDTO created = telefoneService.addToUser(usuarioId, requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/telefones/" + (created != null ? created.id() : "")))
                .body(created);
    }

    // UPDATE TELEFONE
    @PutMapping("/{id}")
    public ResponseEntity<TelefoneResponseDTO> update(@PathVariable Long id,
                                                       @RequestBody TelefoneRequestDTO requestDTO) {
        TelefoneResponseDTO updated = telefoneService.update(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    // DELETE TELEFONE BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        telefoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}