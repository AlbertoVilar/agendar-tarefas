package com.vilardev.Daily.repositories;

import com.vilardev.Daily.infrastructury.entities.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
    java.util.List<Telefone> findByUsuario_Id(Long usuarioId);
    Page<Telefone> findByUsuario_Id(Long usuarioId, Pageable pageable);
}
