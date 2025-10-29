package com.vilardev.Daily.controllers;

import com.vilardev.Daily.dtos.TelefoneRequestDTO;
import com.vilardev.Daily.dtos.TelefoneResponseDTO;
import com.vilardev.Daily.services.TelefoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/usuarios/{usuarioId}/telefones")
public class TelefoneController {

    private final TelefoneService telefoneService;

    public TelefoneController(TelefoneService telefoneService) {
        this.telefoneService = telefoneService;
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
}

