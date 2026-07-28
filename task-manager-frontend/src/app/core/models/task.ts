// No angular e typescript, usamos interface para definir o modelo de dados que
// são enviados para a API (RequestDTO) e que são recebidos da API (ResponseDTO). 

// interface para os dados da tarefa que serão enviados para a API
export interface Task {
    id: number;
    titulo: string;
    descricao?: string;
    concluida: boolean;
    dataCriacao?: string;
}

// interface para os dados da tarefa que serão enviados para a API
export interface TaskRequestDTO {
    titulo: string;
    descricao?: string;
    concluida?: boolean;
}

// interface para os dados da tarefa que serão recebidos da API
export interface TaskResponseDTO {
    id: number;
    titulo: string;
    descricao?: string;
    concluida: boolean;
    dataCriacao?: string;
}

export interface AiSummaryResponseDTO {
    resumo: string;
    statusConexao: 'ONLINE' | 'OFFLINE' | 'SEM_TAREFAS' | 'ERRO';
}