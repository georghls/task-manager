package com.projetosbackend.task_manager.service;

import com.projetosbackend.task_manager.dto.AiGenerateResponseDTO;
import com.projetosbackend.task_manager.dto.AiSummaryDTO;
import com.projetosbackend.task_manager.dto.TaskResponseDTO;
import com.projetosbackend.task_manager.model.Task;
import com.projetosbackend.task_manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private final TaskRepository taskRepository;
    private final RestClient restClient;

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.model}")
    private String apiModel;

    public AIService(TaskRepository taskRepository) {
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

    /**
     * Recebe um texto livre do usuário, envia para a IA interpretar,
     * e salva automaticamente as tarefas geradas no banco.
     */
    public AiGenerateResponseDTO gerarTarefasAutomaticamente(String promptUsuario) {
        String promptSistema = "Você é um assistente de produtividade. "
                + "O usuário vai descrever o que ele precisa fazer em texto livre. "
                + "Sua tarefa é interpretar o texto e extrair uma lista de tarefas. "
                + "Para cada tarefa, defina um 'titulo' curto e claro e uma 'descricao' detalhada. "
                + "Responda APENAS com um JSON válido no seguinte formato, sem nenhum texto extra:\n"
                + "[{\"titulo\": \"...\", \"descricao\": \"...\"}, {\"titulo\": \"...\", \"descricao\": \"...\"}]";

        Map<String, Object> systemMessage = Map.of("role", "system", "content", promptSistema);
        Map<String, Object> userMessage = Map.of("role", "user", "content", promptUsuario);
        Map<String, Object> requestBody = Map.of(
                "model", apiModel,
                "messages", List.of(systemMessage, userMessage),
                "max_tokens", 1000,
                "temperature", 0.3
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

            String jsonResposta = extrairTextoResposta(response);

            // Limpa possíveis marcadores de código markdown
            jsonResposta = jsonResposta.trim();
            if (jsonResposta.startsWith("```")) {
                jsonResposta = jsonResposta.replaceAll("```json\\s*", "").replaceAll("```\\s*", "");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> tarefasParseadas = objectMapper.readValue(
                    jsonResposta,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class)
            );

            List<TaskResponseDTO> tarefasCriadas = new ArrayList<>();
            for (Map<String, String> tarefaMap : tarefasParseadas) {
                Task novaTarefa = new Task();
                novaTarefa.setTitulo(tarefaMap.getOrDefault("titulo", "Tarefa sem título"));
                novaTarefa.setDescricao(tarefaMap.getOrDefault("descricao", ""));
                Task tarefaSalva = taskRepository.save(novaTarefa);
                tarefasCriadas.add(new TaskResponseDTO(tarefaSalva));
            }

            String mensagem = String.format("✅ %d tarefa(s) criada(s) com sucesso pela IA!", tarefasCriadas.size());
            return new AiGenerateResponseDTO(tarefasCriadas, mensagem);

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar tarefas com IA: " + e.getMessage());
            e.printStackTrace();
            return new AiGenerateResponseDTO(
                    List.of(),
                    "Erro ao gerar tarefas: " + e.getMessage()
            );
        }
    }

}
