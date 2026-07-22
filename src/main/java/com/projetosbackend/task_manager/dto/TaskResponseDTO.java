package com.projetosbackend.task_manager.dto;

import com.projetosbackend.task_manager.model.Task;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String titulo,
        String descricao,
        boolean concluida,
        LocalDateTime dataCriacao
) {
    // Construtor auxiliar para converter da Entity Task para DTO de resposta
    public TaskResponseDTO(Task task) {
        this(task.getId(), task.getTitulo(), task.getDescricao(), task.isConcluida(), task.getDataCriacao());
    }
}
