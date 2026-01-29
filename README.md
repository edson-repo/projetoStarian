# STARIAN1 — Gerador e Registro de Observações (Spring Boot)

Aplicação **Spring Boot (Java 8)** que recebe uma lista de números de notas fiscais, **gera uma observação textual padronizada** e **salva o resultado no banco**.

Além da API REST, o projeto também entrega uma **tela simples** (HTML/JS) em `/` para testar pelo navegador.

---

## O que o sistema faz

Você envia uma lista de notas (ex.: `[1, 2, 3]`) e o sistema devolve:

- `Fatura das notas fiscais de simples remessa: 1, 2 e 3.`

Além disso, a observação é registrada no banco como:

- `notas` (string com a lista, ex.: `"[1, 2, 3]"`)
- `resposta` (texto gerado)
- `dataCriacao` (preenchida automaticamente)

---

## Endpoints

### 1) Gerar e salvar

**POST** `/observacoes/gerar`

**Body (JSON):**
```json
{ "notas": [1, 2, 3] }
```

**Resposta (texto/plain):**
```
Fatura das notas fiscais de simples remessa: 1, 2 e 3.
```

Se o payload for inválido (ex.: lista vazia), o sistema devolve **HTTP 400** com um JSON padronizado (veja “Tratamento de erros”).

### 2) Listar tudo que já foi salvo

**GET** `/observacoes/getAll`

**Resposta (JSON):**
```json
[
  {
    "id": 1,
    "notas": "[1, 2, 3]",
    "resposta": "Fatura das notas fiscais de simples remessa: 1, 2 e 3.",
    "dataCriacao": "2026-01-28T12:34:56.789"
  }
]
```

---

## Interface Web (front simples)

Arquivos estáticos em:

- `src/main/resources/static/index.html`
- `src/main/resources/static/js/app.js`
- `src/main/resources/static/css/styles.css`

Ao rodar a aplicação, abra:

- `http://localhost:8080/`

A página faz chamadas para:

- `POST /observacoes/gerar`
- `GET /observacoes/getAll`

---

## Como rodar localmente

### Pré-requisitos

- **Java 8**
- **Maven 3.x**

### Rodar com Maven

```bash
mvn spring-boot:run
```

Depois acesse:

- `http://localhost:8080/`

### Empacotar e rodar

O projeto está configurado para empacotar como **WAR**.

```bash
mvn package
java -jar target/*.war
```

---

## Testes

```bash
mvn test
```

O projeto usa:

- **JUnit 4**
- **Mockito**
- **MockMvc** (para testar Controller sem subir o Spring inteiro)

---

## Banco de dados

O projeto inclui dependências de:

- **H2** (banco embutido, ótimo para rodar local/testes)
- **MySQL** (para quando você quiser ligar em um banco real)

Nos testes (`src/test/resources/application-test.properties`), o H2 roda em memória com:

- `spring.jpa.hibernate.ddl-auto=create-drop`

### Exemplo de configuração (opcional)

Se você quiser configurar MySQL (ou definir `ddl-auto` no ambiente local), crie um arquivo:

- `src/main/resources/application.properties`

Exemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/starian
spring.datasource.username=root
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
```

---

## Tratamento de erros (Global Exception Handler)

Quando a regra de negócio detecta requisição inválida, ela lança `BadRequestException`.
O `GlobalExceptionHandler` converte isso em uma resposta padronizada.

Exemplo (HTTP 400):

```json
{
  "timestamp": "2026-01-28T12:34:56.789",
  "status": 400,
  "error": "Bad Request",
  "message": "A lista 'notas' não pode ser vazia.",
  "path": "/observacoes/gerar"
}
```

Se acontecer um erro inesperado, o handler retorna **HTTP 500** com uma mensagem amigável.

---

## Estrutura de pacotes

- `br.com.softplan.report`
  - `Application` — classe `main` do Spring Boot
- `br.com.softplan.report.controller`
  - `ObservacaoController` — endpoints REST
- `br.com.softplan.report.service`
  - `ObservacaoService` — regra de negócio (gera texto + salva)
- `br.com.softplan.report.repository`
  - `ObservacaoRepository` — acesso ao banco via Spring Data JPA
- `br.com.softplan.report.model`
  - `ObservacaoEntity` — entidade JPA persistida
  - `NotaFiscal` — modelo simples (exemplo/apoio)
- `br.com.softplan.report.dto`
  - `ObservacaoRequest` — body do POST
  - `ObservacaoResponse` — DTO de retorno (disponível para evoluções)
  - `ApiError` — formato de erro da API
- `br.com.softplan.report.exception`
  - `BadRequestException` — erro de validação da regra
  - `GlobalExceptionHandler` — padroniza respostas de erro
- `br.com.softplan.report.application`
  - `GeradorObservacao` — versão **legada** do algoritmo (mantida para estudo/refatoração)

---

## Docker

O `Dockerfile` faz build com Maven e gera um container que executa o `.war`.

```bash
docker build -t starian1 .
docker run --rm -p 8080:8080 starian1
```

Acesse:

- `http://localhost:8080/`

---

## Observações de estudo (por que algumas escolhas)

- **Camadas (Controller → Service → Repository)**: separa responsabilidades e deixa o código mais fácil de manter e testar.
- **Spring Data JPA (`JpaRepository`)**: evita escrever CRUD “na mão”.
- **`@RestControllerAdvice`**: remove `try/catch` repetido nos controllers e padroniza os erros.
- **H2**: facilita rodar sem instalar banco.
- **MockMvc + Mockito**: permite testar o controller “de ponta a ponta” (controller → service) sem subir a aplicação toda.

---

## Próximos passos (melhorias sugeridas)

- Trocar `String notas = lista.toString()` por um formato mais controlado (ex.: JSON) ou armazenar em tabela relacionada.
- Padronizar validação: hoje um método retorna mensagem e outro lança exceção; ideal é ter **um padrão único**.
- Usar **injeção por construtor** em vez de `@Autowired` em campo (mais testável/limpo).
- Remover dependências duplicadas e alinhar versões no `pom.xml` (há redundâncias em dependências de teste).

---

## Licença

Defina a licença desejada (ex.: MIT) e adicione um arquivo `LICENSE` se for publicar.
