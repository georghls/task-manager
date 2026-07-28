package com.projetosbackend.task_manager.service;

import com.projetosbackend.task_manager.dto.AiSummaryDTO;
import com.projetosbackend.task_manager.model.Task;
import com.projetosbackend.task_manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiAIService {

    private final TaskRepository taskRepository;
    private final RestClient restClient;

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.model}")
    private String apiModel;

    public GeminiAIService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.restClient = RestClient.create();
    }

    public AiSummaryDTO gerarResumoTarefas() {
        List<Task> tarefas = taskRepository.findAll();

        if (tarefas.isEmpty()) {
            return new AiSummaryDTO("Nenhuma tarefa encontrada. Cadastre tarefas para gerar a análise!", "SEM_TAREFAS");
        }

        long concluidas = tarefas.stream().filter(Task::isConcluida).count();
        long pendentes = tarefas.size() - concluidas;

        StringBuilder sb = new StringBuilder();
        sb.append("Aqui estão as tarefas do usuário:\n");
        sb.append(String.format("- Total: %d tarefas (%d concluídas, %d pendentes)\n", tarefas.size(), concluidas, pendentes));
        sb.append("Lista de Tarefas:\n");
        for (Task t : tarefas) {
            sb.append(String.format("* [%s] %s %s\n",
                    t.isConcluida() ? "CONCLUÍDA" : "PENDENTE",
                    t.getTitulo(),
                    t.getDescricao() != null && !t.getDescricao().isBlank() ? "(" + t.getDescricao() + ")" : ""
            ));
        }

        String prompt = "Você é um assistente pessoal de produtividade amigável. "
                + "Analise a lista de tarefas a seguir e faça um resumo bem curto em Português (máximo 3 frases). "
                + "Destaque o progresso atual e dê uma dica motivacional rápida para focar nas tarefas pendentes.\n\n"
                + sb.toString();

        // Estrutura de JSON compatível com OpenAI/Groq API
        Map<String, Object> userMessage = Map.of("role", "user", "content", prompt);
        Map<String, Object> requestBody = Map.of(
                "model", apiModel,
                "messages", List.of(userMessage),
                "max_tokens", 500
        );

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            String resumoExtraido = extrairTextoResposta(response);
            return new AiSummaryDTO(resumoExtraido, "ONLINE");

        } catch (Exception e) {
            System.err.println("❌ Erro detalhado na chamada à API de IA: " + e.getMessage());
            e.printStackTrace();
            return new AiSummaryDTO(
                    "Erro ao se conectar com a API de IA: " + e.getMessage(),
                    "ERRO_API"
            );
        }
    }

    /**
     * Método auxiliar para navegar pelo JSON retornado da API (formato OpenAI/Groq)
     */
    @SuppressWarnings("unchecked")
    private String extrairTextoResposta(Map<String, Object> response) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null) {
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao parsear JSON da API: " + e.getMessage());
        }
        return "Resumo gerado, porém o formato da resposta veio diferente do esperado.";
    }
}
