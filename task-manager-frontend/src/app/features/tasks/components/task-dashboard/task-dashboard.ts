import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { TaskService } from '../../../../core/services/task';
import { TaskResponseDTO } from '../../../../core/models/task';
import { TaskList } from '../task-list/task-list';

@Component({
  selector: 'app-task-dashboard',
  standalone: true,
  imports: [TaskList],
  templateUrl: './task-dashboard.html',
  styleUrl: './task-dashboard.css',
})
export class TaskDashboard implements OnInit {
  // Injeta o nosso serviço HTTP de tarefas
  private readonly taskService = inject(TaskService);

  // Signal principal que armazena a lista de tarefas vindas do backend
  tasks = signal<TaskResponseDTO[]>([]);

  // Signal de estado de carregamento
  loading = signal<boolean>(true);

  // Signals computados automaticamente com base na lista 'tasks'
  totalTasks = computed(() => this.tasks().length);
  pendingTasks = computed(() => this.tasks().filter(t => !t.concluida).length);
  completedTasks = computed(() => this.tasks().filter(t => t.concluida).length);

  ngOnInit(): void {
    this.loadTasks();
  }

  /**
   * Busca a lista de tarefas na API backend Spring Boot
   */
  loadTasks(): void {
    this.loading.set(true);
    this.taskService.getTasks().subscribe({
      next: (data) => {
        this.tasks.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar tarefas:', err);
        this.loading.set(false);
      }
    });
  }

  /**
   * Alterna o status da tarefa no backend (PATCH /tasks/{id}/concluir)
   */
  handleToggleComplete(id: number): void {
    this.taskService.toggleTaskCompletion(id).subscribe({
      next: () => this.loadTasks(), // Recarrega as tarefas atualizadas
      error: (err) => console.error('Erro ao alternar status da tarefa:', err)
    });
  }
  /**
   * Exclui a tarefa no backend (DELETE /tasks/{id})
   */
  handleDeleteTask(id: number): void {
    this.taskService.deleteTask(id).subscribe({
      next: () => this.loadTasks(), // Recarrega as tarefas atualizadas
      error: (err) => console.error('Erro ao excluir tarefa:', err)
    });
  }
}
