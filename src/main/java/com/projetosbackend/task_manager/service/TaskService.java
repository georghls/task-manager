package com.projetosbackend.task_manager.service;

import com.projetosbackend.task_manager.dto.TaskRequestDTO;
import com.projetosbackend.task_manager.dto.TaskResponseDTO;
import com.projetosbackend.task_manager.exception.TaskNotFoundException;
import com.projetosbackend.task_manager.model.Task;
import com.projetosbackend.task_manager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Diz ao Spring: "essa classe é um serviço, gerencie ela pra mim"
public class TaskService {
    private final TaskRepository taskRepository; // O Service precisa do Repository para acessar o banco
    // final - depois de definido, não pode ser trocado. Isso garante que o repository nunca mude durante a vida do Service

    // Injetor de dependencias
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 1. Criar: recebe RequestDTO, converte em Entity, salva, e retorna ResponseDTO
    public TaskResponseDTO criar(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitulo(dto.titulo());
        task.setDescricao(dto.descricao());
        Task taskSalva = taskRepository.save(task);
        return new TaskResponseDTO(taskSalva);
    }
    // 2. Listar todas: converte a lista de Entity para lista de ResponseDTO
    public List<TaskResponseDTO> listarTodas() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponseDTO::new) // Converte cada Task para TaskResponseDTO
                .toList();
    }
    // Metodo auxiliar interno para buscar a Entity Task pelo ID
    private Task buscarEntityPorId(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
    // 3. Buscar por ID: retorna ResponseDTO
    public TaskResponseDTO buscarPorId(Long id) {
        Task task = buscarEntityPorId(id);
        return new TaskResponseDTO(task);
    }
    // 4. Atualizar: recebe ID e RequestDTO, atualiza a Entity e retorna ResponseDTO
    public TaskResponseDTO atualizar(Long id, TaskRequestDTO dto) {
        Task taskExistente = buscarEntityPorId(id);
        taskExistente.setTitulo(dto.titulo());
        taskExistente.setDescricao(dto.descricao());
        Task taskAtualizada = taskRepository.save(taskExistente);
        return new TaskResponseDTO(taskAtualizada);
    }
    // 5. Concluir: marca como concluída e retorna ResponseDTO
    public TaskResponseDTO concluir(Long id) {
        Task task = buscarEntityPorId(id);
        task.setConcluida(true);
        Task taskConcluida = taskRepository.save(task);
        return new TaskResponseDTO(taskConcluida);
    }
    // 6. Deletar
    public void deletar(Long id) {
        Task task = buscarEntityPorId(id);
        taskRepository.deleteById(id);
    }
}
