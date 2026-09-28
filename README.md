# Clínica API

![CI](https://github.com/Bifaniii/Clinica-Java/actions/workflows/ci.yml/badge.svg)

API REST para gestão de uma clínica médica: cadastro de pacientes e médicos e agendamento de consultas. Médicos e pacientes herdam de `Usuario` (herança JOINED no JPA) e se autenticam pelo e-mail.

## Tecnologias

- Java 17 e Spring Boot 4
- Spring Web, Spring Data JPA e Bean Validation
- Spring Security (HTTP Basic, senhas com BCrypt, perfis PACIENTE, MEDICO e ADMIN)
- PostgreSQL
- JUnit 5, Mockito e MockMvc nos testes, com H2 em memória

## Regras de negócio

- Consulta só pode ser marcada para data futura e exige paciente, médico e descrição.
- Paciente precisa ter idade entre 0 e 120 anos e cidade informada.
- Senhas são sempre gravadas com BCrypt e nunca aparecem nas respostas.
- Médico e paciente recebem o perfil (`MEDICO`/`PACIENTE`) automaticamente no cadastro.
- Recurso inexistente responde 404 e erro de validação responde 400, com corpo padronizado:

```json
{ "status": 404, "mensagem": "Médico não encontrado!" }
```

## Endpoints

| Método | Rota | Descrição |
| :----- | :--- | :-------- |
| POST | `/auth` | Cadastra usuário (público) |
| GET, POST | `/pacientes` | Lista e cadastra pacientes |
| GET, PUT, DELETE | `/pacientes/{id}` | Busca, atualiza e remove paciente |
| GET, POST | `/medicos` | Lista e cadastra médicos |
| GET, PUT, DELETE | `/medicos/{id}` | Busca, atualiza e remove médico |
| GET, POST | `/consultas` | Lista e agenda consultas |
| GET, PUT, DELETE | `/consultas/{id}` | Busca, remarca e cancela consulta |

As rotas fora de `/auth` exigem autenticação (HTTP Basic com e-mail e senha).

## Como rodar

Com um PostgreSQL rodando e o banco `clinica` criado:

```bash
export DB_PASSWORD=sua_senha
./mvnw spring-boot:run
```

`DB_URL` e `DB_USERNAME` têm padrão `jdbc:postgresql://localhost:5432/clinica` e `postgres`.

## Testes

Testes unitários dos services com JUnit 5 e Mockito, e testes dos controllers com MockMvc (status HTTP, validação e tratamento de erros). O teste de contexto usa H2, então não precisa de PostgreSQL:

```bash
./mvnw test
```

O GitHub Actions roda a suíte a cada push na `main` e em pull requests.

## Próximos passos

- Autenticação com JWT (as dependências do jjwt já estão no projeto)
- Autorização por perfil nas rotas
- Conflito de horário na agenda do médico
- Docker Compose com a API e o banco
