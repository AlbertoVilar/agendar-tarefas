package com.vilardev.Daily.infrastructury.entities;


import lombok.*;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
@Builder
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome da Role (ex: ROLE_ADMIN, ROLE_USER)
    @Column(name = "nome_role", length = 50, unique = true)
    private String nomeRole;

    // Implementação obrigatória da interface GrantedAuthority
    // Retorna o nome da Role
    @Override
    public String getAuthority() {
        return nomeRole;
    }
}
