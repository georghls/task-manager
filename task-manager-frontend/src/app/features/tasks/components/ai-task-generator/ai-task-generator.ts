import { Component, inject, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TaskService } from '../../../../core/services/task';
import { AiGenerateResponseDTO } from '../../../../core/models/task';

@Component({
    selector: 'app-ai-task-generator',
    standalone: true,
    imports: [FormsModule],
    templateUrl: './ai-task-generator.html',
    styleUrl: './ai-task-generator.css'
})
export class AiTaskGenerator {
    private readonly taskService = inject(TaskService);

    userPrompt = signal<string>('');
    loading = signal<boolean>(false);
    result = signal<AiGenerateResponseDTO | null>(null);
    errorMessage = signal<string>('');

    tasksGenerated = output<void>();

    generateTasks(): void {
        const prompt = this.userPrompt().trim();
        if (!prompt) return;

        this.loading.set(true);
        this.result.set(null);
        this.errorMessage.set('');

        this.taskService.generateTasksWithAi({ prompt }).subscribe({
            next: (data) => {
                this.result.set(data);
                this.loading.set(false);
                this.userPrompt.set('');
                this.tasksGenerated.emit();
            },
            error: (err) => {
                console.error('Erro ao gerar tarefas com IA:', err);
                this.errorMessage.set('Erro ao se comunicar com a IA. Tente novamente.');
                this.loading.set(false);
            }
        });
    }
}
