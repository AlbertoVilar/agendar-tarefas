package com.vilardev.Daily.repositories;

import com.vilardev.Daily.infrastructury.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNomeRole(String nomeRole);
}