package com.projetosbackend.task_manager.repository;

import com.projetosbackend.task_manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

// Essa é a camada que conversa com o banco de dados

public interface TaskRepository extends JpaRepository<Task, Long>{
}
