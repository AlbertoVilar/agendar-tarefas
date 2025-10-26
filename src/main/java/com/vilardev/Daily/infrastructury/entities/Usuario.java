package com.vilardev.Daily.infrastructury.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "senha")
    private String senha;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // Inicializa a lista para evitar NullPointerException
    private List<Endereco> enderecos = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // Inicializa a lista para evitar NullPointerException
    private List<Telefone> telefones = new java.util.ArrayList<>();


    // MÉTODOS PARA GERENCIAR O RELACIONAMENTO BIDIRECIONAL

    public void addEndereco(Endereco endereco) {
        // 1. Adiciona à lista de endereços do usuário
        this.enderecos.add(endereco);

        // 2. CRÍTICO: Seta a referência de volta, garantindo o relacionamento bidirecional
        endereco.setUsuario(this);
    }

    public void addTelefone(Telefone telefone) {
        // 1. Adiciona à lista de telefones do usuário
        this.telefones.add(telefone);

        // 2. CRÍTICO: Seta a referência de volta
        telefone.setUsuario(this);
    }

}
