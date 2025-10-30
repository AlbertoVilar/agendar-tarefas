package com.vilardev.Daily.repositories;

import com.vilardev.Daily.infrastructury.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface EnderecoRespository extends JpaRepository<Endereco, Long> {
    java.util.List<Endereco> findByUsuario_Id(Long usuarioId);
    Page<Endereco> findByUsuario_Id(Long usuarioId, Pageable pageable);
}
