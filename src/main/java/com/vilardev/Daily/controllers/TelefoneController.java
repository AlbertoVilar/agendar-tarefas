package com.vilardev.Daily.controllers;

import com.vilardev.Daily.dtos.TelefoneRequestDTO;
import com.vilardev.Daily.dtos.TelefoneResponseDTO;
import com.vilardev.Daily.infrastructury.entities.Telefone;
import com.vilardev.Daily.mappers.TelefoneMapper;
import com.vilardev.Daily.services.TelefoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telefones")
public class TelefoneController {

    private final TelefoneService telefoneService;

    public TelefoneController(TelefoneService telefoneService, TelefoneMapper telefoneMapper) {
        this.telefoneService = telefoneService;

    }

    @PostMapping
    public ResponseEntity<TelefoneResponseDTO> create(@RequestBody TelefoneRequestDTO requestDTO) {

        TelefoneResponseDTO responseDTO = telefoneService.creatTelefone(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
