package com.projetosbackend.task_manager.service;

import com.projetosbackend.task_manager.dto.AiSummaryDTO;
import com.projetosbackend.task_manager.model.Task;
import com.projetosbackend.task_manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocalAiService {

    private final TaskRepository taskRepository;
    private final RestClient restClient;

    @Value("${ollama.api.url}")
    private String ollamaUrl;

    @Value("${ollama.model}")
    private String ollamaModel;

    public LocalAiService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.restClient = RestClient.create();
    }

    public AiSummaryDTO gerarResumoTarefas() {
        List<Task> tarefas = taskRepository.findAll();

        if (tarefas.isEmpty()) {
            return new AiSummaryDTO("Nenhuma tarefa encontrada. Cadastre tarefas para que a IA possa gerar uma análise!", "SEM_TAREFAS");
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

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", ollamaModel);
        requestBody.put("prompt", prompt);
        requestBody.put("stream", false);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.post()
                    .uri(ollamaUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("response")) {
                String resumoGerado = (String) response.get("response");
                return new AiSummaryDTO(resumoGerado.trim(), "ONLINE");
            } else {
                return new AiSummaryDTO("Não foi possível processar a resposta da IA.", "ERRO");
            }
        } catch (Exception e) {
            return new AiSummaryDTO(
                    "O servidor de IA local (Ollama) parece estar desligado ou indisponível. Certifique-se de que o comando 'ollama run qwen2.5:1.5b' está em execução.",
                    "OFFLINE"
            );
        }
    }
}
