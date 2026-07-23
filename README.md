# Task Manager API

API REST para gerenciamento de tarefas desenvolvida em Java com Spring Boot

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot** (Spring Web, Spring Data JPA, Validation)
- **H2 Database** (Banco de dados em memória)
- **Lombok**
- **Maven**

## 🚀 Como Executar o Projeto

### Pré-requisitos
- **JDK 21** instalado
- **Git**

### Passos para execução

1. **Clonar o repositório:**
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd task-manager
   ```

2. **Executar a aplicação via Maven Wrapper:**

   * **Windows (PowerShell / CMD):**
     ```powershell
     .\mvnw spring-boot:run
     ```
   * **Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```

3. A aplicação estará disponível em `http://localhost:8080`.

## 🗄️ Banco de Dados (Console H2)

O projeto utiliza o banco de dados em memória **H2** para facilidade de desenvolvimento e testes.

- **URL do Console:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Usuário:** `sa`
- **Senha:** *(em branco)*

## 📌 Endpoints da API

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/tasks` | Lista todas as tarefas |
| `POST` | `/tasks` | Cria uma nova tarefa |
| `GET` | `/tasks/{id}` | Busca uma tarefa por ID |
| `PUT` | `/tasks/{id}` | Atualiza uma tarefa existente |
| `DELETE` | `/tasks/{id}` | Remove uma tarefa |

## 🧪 Como Executar os Testes

```powershell
.\mvnw test
```

---

Desenvolvido para fins de estudo em Java & Spring Boot.