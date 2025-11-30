Aqui estão cenários de teste BDD na linguagem Gherkin para a funcionalidade `/CotacaoMoedaDia` da API PTAX do Banco Central do Brasil.

Com base na documentação da API PTAX do BCB, o recurso `/CotacaoMoedaDia` (provavelmente na estrutura OData: `/odata/CotacaoMoedaDia(moeda=@moeda,dataCotacao=@dataCotacao)`) é usado para obter a cotação de uma moeda específica em uma data determinada.

-----

## 🧪 Cenários de Teste BDD (Gherkin)

### 1\. Busca por Cotação de Moeda com Data Válida

Este cenário verifica o comportamento esperado ao buscar a cotação de uma moeda conhecida para uma data em que os dados estão disponíveis.

```gherkin
Funcionalidade: Busca por Cotação de Moeda do Dia
  Como um usuário da API
  Eu quero obter a cotação de compra e venda de uma moeda específica
  Para uma data de cotação válida

  Cenário: Busca bem-sucedida para o Euro em uma data de dia útil
    Dado que a moeda é "EUR"
    E a data de cotação é uma data válida e com cotação registrada (ex: "10-25-2024")
    Quando eu faço uma requisição GET para /CotacaoMoedaDia com os parâmetros da moeda e data
    Então a resposta deve ter o status code 200 (OK)
    E o corpo da resposta deve conter uma lista de cotações (value)
    E cada cotação na lista deve ter os campos:
      | Campo                | Tipo Esperado |
      | dataHoraCotacao      | String        |
      | cotacaoVenda         | Número        |
      | cotacaoCompra        | Número        |
      | tipoBoletim          | String        |
    E os valores de "cotacaoVenda" e "cotacaoCompra" devem ser maiores que zero
```

-----

### 2\. Busca por Cotação em Data sem Expediente Bancário (Fim de Semana/Feriado)

Este cenário verifica o que acontece quando a data solicitada não é um dia útil e, portanto, não possui cotação registrada.

```gherkin
  Cenário: Busca sem resultado para data de fim de semana
    Dado que a moeda é "USD"
    E a data de cotação é um sábado ou domingo (ex: "11-02-2024")
    Quando eu faço uma requisição GET para /CotacaoMoedaDia com os parâmetros da moeda e data
    Então a resposta deve ter o status code 200 (OK)
    E o corpo da resposta deve conter uma lista de cotações vazia (value: [])
```

-----

### 3\. Busca por Cotação de Moeda com Código Inválido

Este cenário verifica o tratamento de erro ao fornecer um código de moeda que não existe na base de dados do BCB.

```gherkin
  Cenário: Falha ao buscar cotação com código de moeda inválido
    Dado que a moeda é "XXX" (código inexistente)
    E a data de cotação é uma data válida (ex: "10-25-2024")
    Quando eu faço uma requisição GET para /CotacaoMoedaDia com os parâmetros da moeda e data
    Então a resposta deve ter o status code 400 (Bad Request) ou 404 (Not Found)
    E o corpo da resposta deve conter uma mensagem de erro indicando o parâmetro inválido ou a falta de recurso
```

-----

### 4\. Busca por Cotação com Formato de Data Inválido

Este cenário verifica o tratamento de erro ao fornecer uma data em um formato diferente do esperado pela API (geralmente `MM-DD-YYYY` ou `YYYY-MM-DD` para APIs OData).

```gherkin
  Cenário: Falha ao buscar cotação com formato de data inválido
    Dado que a moeda é "JPY"
    E a data de cotação é formatada incorretamente (ex: "25/10/2024")
    Quando eu faço uma requisição GET para /CotacaoMoedaDia com os parâmetros da moeda e data
    Então a resposta deve ter o status code 400 (Bad Request)
    E o corpo da resposta deve conter uma mensagem de erro indicando o formato de data incorreto
```

-----

### 5\. Busca por Cotação sem Parâmetros Requeridos

Este cenário verifica a resposta da API quando um ou mais parâmetros obrigatórios (`moeda` ou `dataCotacao`) estão ausentes na requisição.

```gherkin
  Cenário: Falha ao buscar cotação sem o parâmetro de data
    Dado que a moeda é "AUD"
    E o parâmetro de data de cotação está ausente
    Quando eu faço uma requisição GET para /CotacaoMoedaDia apenas com o parâmetro da moeda
    Então a resposta deve ter o status code 400 (Bad Request)
    E o corpo da resposta deve conter uma mensagem de erro sobre o parâmetro ausente
```

-----

Gostaria de cenários de teste para outra funcionalidade da API PTAX ou para a funcionalidade de **período** (`/CotacaoMoedaPeriodo`)?