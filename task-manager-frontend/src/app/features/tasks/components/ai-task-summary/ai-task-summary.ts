import { Component, inject, signal } from '@angular/core';
import { TaskService } from '../../../../core/services/task';
import { AiSummaryResponseDTO } from '../../../../core/models/task';

@Component({
    selector: 'app-ai-task-summary',
    standalone: true,
    templateUrl: './ai-task-summary.html',
    styleUrl: './ai-task-summary.css'
})
export class AiTaskSummary {
    private readonly taskService = inject(TaskService);

    summary = signal<AiSummaryResponseDTO | null>(null);
    loading = signal<boolean>(false);

    fetchSummary(): void {
        this.loading.set(true);
        this.taskService.getAiSummary().subscribe({
            next: (data) => {
                this.summary.set(data);
                this.loading.set(false);
            },
            error: (err) => {
                console.error('Erro ao buscar resumo da IA:', err);
                this.summary.set({
                    resumo: 'Erro de comunicação com o servidor ao buscar resumo da IA.',
                    statusConexao: 'ERRO'
                });
                this.loading.set(false);
            }
        });
    }
}
