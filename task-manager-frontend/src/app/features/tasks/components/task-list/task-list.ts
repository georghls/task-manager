import { Component, input, output, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaskResponseDTO } from '../../../../core/models/task';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css',
})
export class TaskList {
  // Input reativo recebendo a lista de tarefas do componente pai (TaskDashboard)
  tasks = input<TaskResponseDTO[]>([]);

  // Eventos emitidos para o pai quando o usuário clica para alternar status ou excluir
  toggleComplete = output<number>();
  deleteTask = output<number>();
  editTask = output<TaskResponseDTO>();

  // Signals locais para busca e filtro por status
  searchTerm = signal<string>('');
  filterStatus = signal<'ALL' | 'PENDING' | 'COMPLETED'>('ALL');

  // Signal computado que filtra as tarefas automaticamente conforme a busca e o filtro
  filteredTasks = computed(() => {
    const list = this.tasks();
    const search = this.searchTerm().toLowerCase().trim();
    const status = this.filterStatus();

    return list.filter(task => {
      // Filtro por texto (título ou descrição)
      const matchesSearch = task.titulo.toLowerCase().includes(search) ||
        (task.descricao && task.descricao.toLowerCase().includes(search));

      // Filtro por status
      const matchesStatus = status === 'ALL' ||
        (status === 'COMPLETED' && task.concluida) ||
        (status === 'PENDING' && !task.concluida);

      return matchesSearch && matchesStatus;
    });
  });

  // Métodos para atualizar os filtros
  onSearchChange(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    this.searchTerm.set(inputElement.value);
  }

  setFilter(status: 'ALL' | 'PENDING' | 'COMPLETED'): void {
    this.filterStatus.set(status);
  }

  onEdit(task: TaskResponseDTO): void {
    this.editTask.emit(task);
  }
  onToggle(id: number): void {
    this.toggleComplete.emit(id);
  }

  onDelete(id: number): void {
    if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
      this.deleteTask.emit(id);
    }
  }
}
