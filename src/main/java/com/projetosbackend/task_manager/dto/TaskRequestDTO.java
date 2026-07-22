package com.projetosbackend.task_manager.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        String titulo,
        String descricao
) {}
