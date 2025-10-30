package com.vilardev.Daily.repositories;

import com.vilardev.Daily.infrastructury.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRespository extends JpaRepository<Endereco, Long> {
    java.util.List<Endereco> findByUsuario_Id(Long usuarioId);
}
