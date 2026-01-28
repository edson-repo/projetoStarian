# STARIAN1 — Gerador de Observações de Notas Fiscais

Este repositório contém uma implementação simples (e com testes) de um **gerador de texto de “observação”** a partir de uma lista de números de notas fiscais.

A ideia é transformar uma lista como `[1, 2, 3, 4, 5]` em um texto padronizado como:

- `Fatura das notas fiscais de simples remessa: 1, 2, 3, 4 e 5.`

---

## Estrutura do código

- `br.com.softplan.report.application`
  - **`GeradorObservacao`**: classe responsável por montar a observação a partir de uma lista de números.
  - **`GeradorObservacaoTest`**: testes unitários (JUnit) cobrindo os principais cenários de formatação.

- `br.com.softplan.report.model`
  - **`NotaFiscal`**: modelo simples (imutável) de nota fiscal com `numero` e `valor`.

---

## Como funciona

### Regras de formatação (GeradorObservacao)

Dada uma lista de inteiros:

- Se a lista estiver vazia → retorna string vazia (`""`)
- Se tiver **1** item → usa o texto no singular  
  `Fatura da nota fiscal de simples remessa: X.`
- Se tiver **2 ou mais** itens → usa o texto no plural e aplica separadores:
  - separa com `", "` entre os itens do meio
  - usa `" e "` antes do último item

Exemplos:

- `[1]` → `Fatura da nota fiscal de simples remessa: 1.`
- `[1, 3]` → `Fatura das notas fiscais de simples remessa: 1 e 3.`
- `[1, 2, 3]` → `Fatura das notas fiscais de simples remessa: 1, 2 e 3.`

---

## Rodando os testes

O projeto utiliza **JUnit 4** (anotações `@Test` e `@Before`).

Como o arquivo de build (Maven/Gradle) pode variar conforme seu setup, seguem os comandos mais comuns:

### Maven
```bash
mvn test
```

### Gradle
```bash
./gradlew test
```

---

## Exemplo de uso

```java
GeradorObservacao gerador = new GeradorObservacao();

List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);
String obs = gerador.geraObservacao(numeros);

// obs: "Fatura das notas fiscais de simples remessa: 1, 2, 3, 4 e 5."
```

---

## Decisões e “por que” de algumas escolhas

- **`StringBuilder`**: usado para montar a lista final de números sem criar muitas strings intermediárias.
- **`Iterator`**: facilita verificar se ainda existe “próximo item” (`hasNext()`), o que ajuda a decidir entre `", "` e `" e "`.
- **`BigDecimal` no `NotaFiscal`**: é a escolha correta para valores monetários, pois evita erros de precisão que podem ocorrer com `double/float`.

---

## Melhorias sugeridas (refatoração)

Alguns pontos que podem ser melhorados para deixar o código mais idiomático e seguro:

- Usar generics (`List<Integer>`) ao invés de `List` “cru”.
- Evitar estado na classe (`String t` como atributo). Preferir variáveis locais para tornar o método **thread-safe** e mais fácil de manter.
- Trocar `c.toString() == null` (que nunca acontece) por `c.length() == 0`.
- Renomear variáveis (`umoNota` → `umaNota`, `args1`/`lista2` → nomes mais claros).
- Considerar abordagem com `Streams` (`Collectors.joining`) para simplificar a montagem do texto.

---

## Licença

Defina a licença que você pretende usar (por exemplo, MIT) e adicione um arquivo `LICENSE` se for publicar.
