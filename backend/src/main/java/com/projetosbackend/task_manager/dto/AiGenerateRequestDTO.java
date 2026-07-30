package com.projetosbackend.task_manager.dto;

import jakarta.validation.constraints.NotBlank;

public record AiGenerateRequestDTO(
        @NotBlank(message = "O prompt é obrigatório")
        String prompt
) {}
