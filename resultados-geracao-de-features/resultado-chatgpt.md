A seguir estão **cenários de teste BDD em Gherkin**, completos e bem estruturados, utilizando exclusivamente a funcionalidade **/CotacaoMoedaDia** da API PTAX — conforme a documentação oficial do OLINDA.

Incluo:

* Feature completa
* Background
* Cenários de sucesso
* Cenários de falha (moeda inválida, data sem cotação, parâmetros ausentes)
* Campos esperados conforme o modelo OLINDA: `"cotacaoCompra"`, `"cotacaoVenda"`, `"dataHoraCotacao"`

---

# ✔️ **Feature: Cotação de moeda por dia – API PTAX**

```gherkin
Feature: Consulta de cotação de moeda por dia (PTAX)
  Como usuário da API PTAX
  Quero consultar a cotação de uma moeda em uma data específica
  Para obter valores oficiais de câmbio do Banco Central

  Background:
    Given que o serviço /CotacaoMoedaDia está disponível
    And que a API aceita consultas com os parâmetros "moeda", "dataCotacao" e "$format=json"
```

---

# ✔️ **Cenário 1 — Consulta válida para USD em 13/11/2025**

```gherkin
  Scenario: Consultar cotação válida para USD em 13/11/2025
    When eu consulto a cotação da moeda "USD" para a data "11-13-2025"
    Then a resposta deve conter um registro de cotação
    And a resposta deve conter "cotacaoCompra" e "cotacaoVenda"
    And a data da resposta deve ser "2025-11-13"
```

---

# ✔️ **Cenário 2 — Consulta válida para EUR em 05/05/2024**

```gherkin
  Scenario: Consultar cotação válida para EUR em 05/05/2024
    When eu consulto a cotação da moeda "EUR" para a data "05-05-2024"
    Then a resposta deve conter um registro de cotação
    And deve retornar os campos "cotacaoCompra", "cotacaoVenda" e "dataHoraCotacao"
```

---

# ✔️ **Cenário 3 — Moeda inexistente**

```gherkin
  Scenario: Consultar cotação de moeda inexistente
    When eu consulto a cotação da moeda "XXX" para a data "11-13-2025"
    Then a resposta deve conter zero registros
    And deve retornar uma coleção vazia
```

---

# ✔️ **Cenário 4 — Data sem cotação disponível (ex.: fim de semana ou feriado)**

```gherkin
  Scenario: Consultar cotação para uma data sem operações
    When eu consulto a cotação da moeda "USD" para a data "11-16-2025"
    Then a resposta deve conter zero registros
    And a API não deve retornar valores de compra ou venda
```

*(16/11/2025 é um domingo — a PTAX não divulga cotação.)*

---

# ✔️ **Cenário 5 — Parâmetro de data ausente**

```gherkin
  Scenario: Consultar cotação sem informar a data
    When eu consulto a cotação da moeda "USD" sem informar a data
    Then a API deve retornar erro ou mensagem indicando parâmetro inválido
```

---

# ✔️ **Cenário 6 — Parâmetro de moeda ausente**

```gherkin
  Scenario: Consultar cotação sem informar a moeda
    When eu consulto a cotação para a data "11-13-2025" sem informar a moeda
    Then a API deve retornar erro ou coleção vazia
```

---

# ✔️ **Cenário 7 — Formato inválido da data**

```gherkin
  Scenario: Consultar cotação com data em formato inválido
    When eu consulto a cotação da moeda "USD" para a data "2025/11/13"
    Then a API deve retornar erro de formato de parâmetro
```

---

# ✔️ **Cenário 8 — Verificação dos limites da API**

```gherkin
  Scenario: Consultar cotação com data muito antiga
    When eu consulto a cotação da moeda "USD" para a data "01-01-1900"
    Then a API deve retornar zero registros
    And não deve retornar erro
```

---

# ✔️ Quer que eu gere também?

Posso gerar **Steps Definitions em Java (Cucumber + JUnit 5)**, **Requests reais usando RestAssured**, **estrutura de projeto Maven**, ou **scripts de teste automáticos**.

Deseja que eu gere o código também?
