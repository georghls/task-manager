package com.projetosbackend.task_manager.controller;

import com.projetosbackend.task_manager.dto.AiSummaryDTO;
import com.projetosbackend.task_manager.dto.TaskRequestDTO;
import com.projetosbackend.task_manager.dto.TaskResponseDTO;
import com.projetosbackend.task_manager.service.LocalAiService;
import com.projetosbackend.task_manager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Diz ao Spring que esta classe vai lidar com endpoints HTTP/REST
@RequestMapping("/tasks") // Define que todos os endpoints deste controller começam com /tasks
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {
    private final TaskService taskService;
    private final LocalAiService localAiService;

    public TaskController(TaskService taskService, LocalAiService localAiService) {

        this.taskService = taskService;
        this.localAiService = localAiService;
    }

    @GetMapping("/ai/summary")
    public ResponseEntity<AiSummaryDTO> obterResumoIa() {
        AiSummaryDTO resumo = localAiService.gerarResumoTarefas();
        return ResponseEntity.ok(resumo);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> criar(@Valid @RequestBody TaskRequestDTO dto) {
        TaskResponseDTO novaTask = taskService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTask);
    }
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> listarTodas() {
        List<TaskResponseDTO> tasks = taskService.listarTodas();
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> buscarPorId(@PathVariable Long id) {
        TaskResponseDTO task = taskService.buscarPorId(id);
        return ResponseEntity.ok(task);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO dto) {
        TaskResponseDTO taskAtualizada = taskService.atualizar(id, dto);
        return ResponseEntity.ok(taskAtualizada);
    }
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<TaskResponseDTO> concluir(@PathVariable Long id) {
        TaskResponseDTO taskConcluida = taskService.concluir(id);
        return ResponseEntity.ok(taskConcluida);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        taskService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
