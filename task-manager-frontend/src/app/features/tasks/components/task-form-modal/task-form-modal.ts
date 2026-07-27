import { Component, OnInit, input, output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TaskRequestDTO, TaskResponseDTO } from '../../../../core/models/task';

@Component({
  selector: 'app-task-form-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './task-form-modal.html',
  styleUrl: './task-form-modal.css',
})
export class TaskFormModal implements OnInit {
  private readonly fb = inject(FormBuilder);

  // Recebe a tarefa se for edição ou null se for criação
  taskToEdit = input<TaskResponseDTO | null>(null);

  // Eventos emitidos para o pai (TaskDashboard)
  saveTask = output<TaskRequestDTO>();
  closeModal = output<void>();

  // Grupo do formulário reativo
  taskForm!: FormGroup;

  ngOnInit(): void {
    // Inicializa os controles do formulário com validação de campo obrigatório
    this.taskForm = this.fb.group({
      titulo: [this.taskToEdit()?.titulo || '', [Validators.required, Validators.minLength(3)]],
      descricao: [this.taskToEdit()?.descricao || '']
    });
  }

  get isEditing(): boolean {
    return !!this.taskToEdit();
  }

  onSubmit(): void {
    if (this.taskForm.invalid) {
      this.taskForm.markAllAsTouched();
      return;
    }

    const formValue: TaskRequestDTO = {
      titulo: this.taskForm.value.titulo.trim(),
      descricao: this.taskForm.value.descricao?.trim() || ''
    };

    this.saveTask.emit(formValue);
  }

  onCancel(): void {
    this.closeModal.emit();
  }
}
