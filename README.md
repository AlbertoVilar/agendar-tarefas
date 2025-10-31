# Daily (Spring Boot)

Aplicação Java Spring Boot para gestão diária de tarefas, com autenticação via JWT, autorização por roles e persistência em H2 (perfil de desenvolvimento/teste).

## Visão Geral
- Autenticação: `POST /auth/login` com JSON `{ "email", "senha" }`
- Autorização: JWT Bearer em `Authorization: Bearer <token>`
- Endpoint do usuário logado: `GET /me` (requer `ROLE_ADMIN`)
- Paths protegidos por role: `/admin/**` exige `ROLE_ADMIN`
- Console H2: `/h2-console` (liberado em dev/test)

## Requisitos
- Java 17+
- Maven 3.8+

## Como Rodar
1) Build do projeto
```
mvn clean package -DskipTests
```
2) Executar a aplicação (porta padrão `8080`)
```
java -jar target/daily-0.0.1-SNAPSHOT.jar
```
3) Alternativamente, com perfil e porta explícitos
```
java -jar target/daily-0.0.1-SNAPSHOT.jar --spring.profiles.active=test --server.port=8080
```

### Com Docker Compose

Este projeto já inclui `Dockerfile` multi-stage e `docker-compose.yml` com Postgres.

- Subir banco e app (perfil `postgres`):
```
docker compose up -d --build app
```
- Ver logs da aplicação:
```
docker compose logs -f app
```
- Parar serviços:
```
docker compose down
```

Configurações relevantes (já definidas no compose):
- `SPRING_PROFILES_ACTIVE=postgres`
- `SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/daily`
- `SPRING_DATASOURCE_USERNAME=postgres`
- `SPRING_DATASOURCE_PASSWORD=postgres`
- `CONTROLLERS_USUARIO_ENABLED=true` (habilita endpoints de usuários)

### Usando Postgres

Crie um banco `daily` e configure credenciais (padrão `postgres/postgres`). O perfil `postgres` usa `src/main/resources/application-postgres.properties`.

- Executar com Maven:
```
mvn spring-boot:run -Dspring-boot.run.profiles=postgres -DskipTests
```
- Executar o JAR com perfil:
```
java -jar target/daily-0.0.1-SNAPSHOT.jar --spring.profiles.active=postgres
```

Propriedades padrão do perfil `postgres`:
- `spring.datasource.url=jdbc:postgresql://localhost:5432/daily`
- `spring.datasource.username=postgres`
- `spring.datasource.password=postgres`
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.sql.init.mode=always` (semeia `data.sql` com roles)

Opcional via Docker:
```
docker run --name daily-pg -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=daily -p 5432:5432 -d postgres:16
```

Com `docker compose`, não é necessário executar o comando acima; o serviço `db` será criado automaticamente.

## Seed de Roles
O projeto carrega apenas as roles via `data.sql`:
- `ROLE_USER`
- `ROLE_ADMIN`

Não há mais usuário padrão. Crie um usuário via API `POST /auth/register` passando `nome`, `email` e `senha` (endereços/telefones opcionais). A senha é criptografada pelo serviço na criação.

## Autenticação e Autorização
- Login: `POST /auth/login`
  - Body JSON obrigatório: `{"email":"<seu-email>", "senha":"<sua-senha>"}`
  - Retorna token JWT em `token` e informações do usuário
- Uso do token: inclua o header `Authorization: Bearer <token>` nas requisições protegidas
- Regras atuais:
  - Público: `/auth/**`, `/h2-console/**`
  - Protegido por role: `/admin/**` requer `ROLE_ADMIN`
  - Método anotado: `GET /me` exige `ROLE_ADMIN` via `@PreAuthorize("hasRole('ADMIN')")`

## Endpoints Principais
- `POST /auth/login`
  - Body: `{"email":"albertovilar1@gmail.com","senha":"132747"}`
  - 200: retorna JWT e dados do usuário
  - 401: credenciais inválidas ou body inválido (campo `senha` é obrigatório; não use `password`)

- `GET /me`
  - Header: `Authorization: Bearer <token>`
  - 200: retorna o usuário autenticado (apenas se tiver `ROLE_ADMIN`)
  - 403: autenticado mas sem `ROLE_ADMIN`
  - 401: sem token Bearer ou token inválido

## Exemplos com curl
Registro, login e captura do token:
```
curl -s -X POST "http://localhost:8080/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Alberto","email":"alberto@example.com","senha":"132747"}'
```

Agora faça o login:
```
curl -s -X POST "http://localhost:8080/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"albertovilar1@gmail.com","senha":"132747"}'
```
Usando o token no `/me`:
```
curl -s -X GET "http://localhost:8080/me" \
  -H "Authorization: Bearer <TOKEN_JWT>"
```

## Dicas para Postman
- Crie uma request `POST /auth/login` e salve o `token` em variável de ambiente:
  - Aba Tests (exemplo):
```
pm.environment.set("token", pm.response.json().token);
```
- Em `GET /me`, use `Authorization` = `Bearer Token` com `{{token}}`
- Garanta método correto: `/auth/login` é `POST`, `/me` é `GET`
- Use `Content-Type: application/json` no login

## Erros Comuns e Soluções
- 401 no login: body inválido (use `email` e `senha`), cabeçalho ausente ou senha incorreta
- 401 no `/me`: faltou `Authorization: Bearer <token>` ou token inválido/expirado
- 403 no `/me`: usuário autenticado, mas sem `ROLE_ADMIN`
- Método incorreto no `/me`: enviar `POST` em vez de `GET` retorna 401/404

## H2 Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL, usuário e senha conforme `application.properties` (padrão H2 em memória quando ativo)
- Em `SecurityConfig`, `/h2-console/**` está liberado e com headers ajustados para frames

## Testes
- Rodar testes:
```
mvn test
```

## Notas de Segurança
- Tokens JWT devem ser tratados como sigilosos; não versionar nem expor em logs públicos
- Em produção, configurar secret do JWT via variável de ambiente/propriedade segura

## Contribuição
- Crie branchs a partir de `develop`
- Padronize mensagens de commit (ex.: `feat:`, `fix:`, `docs:`)
- Abra PR descrevendo mudanças e passos de teste