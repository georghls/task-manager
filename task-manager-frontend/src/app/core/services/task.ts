import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AiSummaryResponseDTO, TaskRequestDTO, TaskResponseDTO } from '../models/task';
import { environment } from '../../../environments/environments'; // Ajuste o caminho se seu arquivo for environment.ts

@Injectable({
    providedIn: 'root'
})
export class TaskService {
    // Injeta o cliente HTTP do Angular
    private readonly http = inject(HttpClient);

    // URL base apontando para http://localhost:8080/tasks
    private readonly apiUrl = `${environment.apiUrl}/tasks`;

    /**
     * GET /tasks
     * Busca todas as tarefas cadastradas no backend.
     */
    getTasks(): Observable<TaskResponseDTO[]> {
        return this.http.get<TaskResponseDTO[]>(this.apiUrl);
    }

    /**
     * GET /tasks/{id}
     * Busca uma tarefa específica pelo ID.
     */
    getTaskById(id: number): Observable<TaskResponseDTO> {
        return this.http.get<TaskResponseDTO>(`${this.apiUrl}/${id}`);
    }

    /**
     * POST /tasks
     * Envia uma nova tarefa para ser salva no banco de dados.
     */
    createTask(dto: TaskRequestDTO): Observable<TaskResponseDTO> {
        return this.http.post<TaskResponseDTO>(this.apiUrl, dto);
    }

    /**
     * PUT /tasks/{id}
     * Atualiza o título e/ou a descrição de uma tarefa existente.
     */
    updateTask(id: number, dto: TaskRequestDTO): Observable<TaskResponseDTO> {
        return this.http.put<TaskResponseDTO>(`${this.apiUrl}/${id}`, dto);
    }

    /**
     * PATCH /tasks/{id}/concluir
     * Alterna o status da tarefa (concluída / pendente).
     */
    toggleTaskCompletion(id: number): Observable<TaskResponseDTO> {
        return this.http.patch<TaskResponseDTO>(`${this.apiUrl}/${id}/concluir`, {});
    }

    /**
     * DELETE /tasks/{id}
     * Exclui permanentemente uma tarefa do sistema.
     */
    deleteTask(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
    /**
     * GET /tasks/ai/summary
     * Obtém o resumo gerado pela IA local (Ollama / Qwen2.5)
     */
    getAiSummary(): Observable<AiSummaryResponseDTO> {
        return this.http.get<AiSummaryResponseDTO>(`${this.apiUrl}/ai/summary`);
    }

}
