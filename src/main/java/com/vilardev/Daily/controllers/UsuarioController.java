package com.vilardev.Daily.controllers;

import com.vilardev.Daily.dtos.UsuarioRequestDTO;
import com.vilardev.Daily.dtos.UsuarioResponseDTO;
import com.vilardev.Daily.dtos.UsuarioUpdateDTO;
import com.vilardev.Daily.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(
        name = "controllers.usuario.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(@RequestBody UsuarioRequestDTO requestDTO) {
        // TODO: implementar lógica no service
        UsuarioResponseDTO created = usuarioService.createUser(requestDTO);
        // TODO: ajustar Location com o ID real retornado
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/usuarios/" + (created != null ? created.id() : "")))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Long id) {
        // TODO: implementar lógica no service
        UsuarioResponseDTO dto = usuarioService.getUserById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> list() {
        // TODO: implementar lógica no service
        List<UsuarioResponseDTO> list = usuarioService.listUsers();
        return ResponseEntity.ok(list);
    }

    // Atualiza os dados de um usuário por ID (ADMIN ou fluxo específico)
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateById(@PathVariable Long id,
                                                         @RequestBody UsuarioUpdateDTO dto) {
        UsuarioResponseDTO updated = usuarioService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }


    // Atualiza os dados do usuário autenticado (email extraído do token)
    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> updateMe(@RequestBody UsuarioUpdateDTO dto) {
        UsuarioResponseDTO updated = usuarioService.updateMe(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // TODO: implementar lógica no service
        usuarioService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}