package com.projetosbackend.task_manager.dto;

import java.util.List;

public class AiGenerateResponseDTO {
    private List<TaskResponseDTO> tarefasCriadas;
    private String mensagem;
    private int quantidadeCriada;

    public AiGenerateResponseDTO() {}

    public AiGenerateResponseDTO(List<TaskResponseDTO> tarefasCriadas, String mensagem) {
        this.tarefasCriadas = tarefasCriadas;
        this.mensagem = mensagem;
        this.quantidadeCriada = tarefasCriadas.size();
    }

    public List<TaskResponseDTO> getTarefasCriadas() { return tarefasCriadas; }
    public void setTarefasCriadas(List<TaskResponseDTO> tarefasCriadas) { this.tarefasCriadas = tarefasCriadas; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public int getQuantidadeCriada() { return quantidadeCriada; }
    public void setQuantidadeCriada(int quantidadeCriada) { this.quantidadeCriada = quantidadeCriada; }
}
