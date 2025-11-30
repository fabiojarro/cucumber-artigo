Claro! Para a funcionalidade **`/CotacaoMoedaDia`** da API PTAX do Banco Central, podemos estruturar cenários de teste em **BDD (Behavior Driven Development)** usando a linguagem **Gherkin**. Esses cenários validam diferentes comportamentos esperados da API, como consultas bem-sucedidas, erros de parâmetros e ausência de dados.  

Aqui está um conjunto de exemplos:

---

## 📝 Cenários de Teste BDD em Gherkin

```gherkin
Feature: Consulta de cotação de moeda por dia
  Como consumidor da API PTAX
  Quero consultar a cotação de uma moeda em uma data específica
  Para obter valores oficiais de câmbio

  Background:
    Given que a API PTAX está disponível

  Scenario: Consulta bem-sucedida da cotação do Dólar em uma data válida
    When eu consulto a cotação da moeda "USD" para a data "2023-11-01"
    Then a resposta deve conter a cotação de compra e venda
    And o status da resposta deve ser 200

  Scenario: Consulta de moeda inexistente
    When eu consulto a cotação da moeda "ABC" para a data "2023-11-01"
    Then a resposta deve indicar que a moeda não foi encontrada
    And o status da resposta deve ser 404

  Scenario: Consulta em data sem cotação disponível (feriado ou fim de semana)
    When eu consulto a cotação da moeda "USD" para a data "2023-12-25"
    Then a resposta deve indicar ausência de dados
    And o status da resposta deve ser 200
    And o corpo da resposta deve estar vazio

  Scenario: Consulta com formato de data inválido
    When eu consulto a cotação da moeda "USD" para a data "25-12-2023"
    Then a resposta deve indicar erro de parâmetro
    And o status da resposta deve ser 400

  Scenario: Consulta sem informar a moeda
    When eu consulto a cotação sem informar o parâmetro "moeda"
    Then a resposta deve indicar erro de requisição
    And o status da resposta deve ser 400

  Scenario: Consulta sem informar a data
    When eu consulto a cotação da moeda "USD" sem informar a data
    Then a resposta deve indicar erro de requisição
    And o status da resposta deve ser 400
```

---

## 🔍 Explicação dos cenários
- **Consulta bem-sucedida** → valida retorno correto com dados de compra e venda.  
- **Moeda inexistente** → garante que moedas não cadastradas retornem erro.  
- **Data sem cotação** → cobre casos de feriados/fins de semana.  
- **Formato inválido de data** → valida tratamento de parâmetros incorretos.  
- **Parâmetros ausentes** → cobre casos de requisição incompleta.  

---

Quer que eu também monte **exemplos de tabelas de dados (`Scenario Outline`)** para testar múltiplas moedas e datas em um único cenário? Isso deixaria os testes ainda mais reutilizáveis.