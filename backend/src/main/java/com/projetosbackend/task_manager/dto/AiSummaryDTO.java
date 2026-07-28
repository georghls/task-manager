package com.projetosbackend.task_manager.dto;

public class AiSummaryDTO {
    private String resumo;
    private String statusConexao;

    public AiSummaryDTO() {}

    public AiSummaryDTO(String resumo, String statusConexao) {
        this.resumo = resumo;
        this.statusConexao = statusConexao;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public String getStatusConexao() {
        return statusConexao;
    }

    public void setStatusConexao(String statusConexao) {
        this.statusConexao = statusConexao;
    }
}
