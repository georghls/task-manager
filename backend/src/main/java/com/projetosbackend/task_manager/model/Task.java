package com.projetosbackend.task_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity // Diz ao spring que essa classe representa uma tabela no banco
@Getter
@Setter
@NoArgsConstructor // cria construtor vazio para o JPA
@AllArgsConstructor // cria o objeto passando tudo de uma vez

public class Task {
    @Id // Diz ao JPA que esse campo é a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.AUTO) // Gera o valor do id automaticamente
    private Long id; // suporta valores muito maiores

    @NotBlank(message = "o título é obrigatório") // rejeita tasks sem titulos
    private String titulo;

    private String descricao;

    private boolean concluida = false;

    private LocalDateTime dataCriacao;

    @PrePersist // Ela marca um metodo que é executado automaticamente antes do objeto ser salvo no banco pela primeira vez
    protected void onCreate() { // acesso protected: A própria classe + classes filhas (herança) + classes do mesmo pacote. é a prática mais comum para métodos de ciclo de vida do JPA
        this.dataCriacao = LocalDateTime.now();
    }
}
