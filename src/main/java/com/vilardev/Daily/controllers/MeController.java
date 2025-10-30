package com.vilardev.Daily.controllers;

import com.vilardev.Daily.dtos.UsuarioResponseDTO;
import com.vilardev.Daily.dtos.UsuarioUpdateDTO;
import com.vilardev.Daily.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class MeController {

    private final UsuarioService usuarioService;

    public MeController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Atualiza os dados do usuário autenticado (email extraído do token)
    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> updateMe(@RequestBody UsuarioUpdateDTO dto) {
        UsuarioResponseDTO updated = usuarioService.updateMe(dto);
        return ResponseEntity.ok(updated);
    }
}