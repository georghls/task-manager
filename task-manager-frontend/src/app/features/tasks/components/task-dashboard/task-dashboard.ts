import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { TaskService } from '../../../../core/services/task';
import { TaskRequestDTO, TaskResponseDTO } from '../../../../core/models/task';
import { TaskList } from '../task-list/task-list';
import { TaskFormModal } from '../task-form-modal/task-form-modal';

@Component({
  selector: 'app-task-dashboard',
  standalone: true,
  imports: [TaskList, TaskFormModal],
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

  // Signals para controlar o estado do Modal
  isModalOpen = signal<boolean>(false);
  taskToEdit = signal<TaskResponseDTO | null>(null);

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

  // Métodos de Abertura e Fechamento do Modal
  openCreateModal(): void {
    this.taskToEdit.set(null);
    this.isModalOpen.set(true);
  }

  openEditModal(task: TaskResponseDTO): void {
    this.taskToEdit.set(task);
    this.isModalOpen.set(true);
  }
  closeModal(): void {
    this.isModalOpen.set(false);
    this.taskToEdit.set(null);
  }

  /**
   * Salva a tarefa (Criação ou Edição) na API Spring Boot
   */
  handleSaveTask(dto: TaskRequestDTO): void {
    const currentTask = this.taskToEdit();
    if (currentTask) {
      // Edição (PUT /tasks/{id})
      this.taskService.updateTask(currentTask.id, dto).subscribe({
        next: () => {
          this.closeModal();
          this.loadTasks();
        },
        error: (err) => console.error('Erro ao atualizar tarefa:', err)
      });
    } else {
      // Criação (POST /tasks)
      this.taskService.createTask(dto).subscribe({
        next: () => {
          this.closeModal();
          this.loadTasks();
        },
        error: (err) => console.error('Erro ao criar tarefa:', err)
      });
    }
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
