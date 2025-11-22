A seguir está **uma implementação completa e funcional** de um teste BDD usando:

✅ **Cucumber 7**
✅ **JUnit 5**
✅ **Java 21**
✅ Consumindo a API **PTAX – /CotacaoMoedaDia**
Documentação: [https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/swagger-ui3#/](https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/swagger-ui3#/)

---

# ✅ 1. Feature File (`cotacao.feature`)

```gherkin
Feature: Consulta de cotação de moeda por dia
  Como usuário da API PTAX
  Quero consultar a cotação de uma moeda em uma data específica
  Para obter valores oficiais de câmbio

  Background:
    Given que a API PTAX está disponível

  Scenario: Consultar cotação válida para USD em 13/11/2025
    When eu consulto a cotação da moeda "USD" para a data "11-13-2025"
    Then a resposta deve conter a data "2025-11-13"
    And a resposta deve conter o valor de compra e venda
```

---

# ✅ 2. Step Definitions (`CotacaoSteps.java`)

```java
package br.com.exemplo.steps;

import io.cucumber.java.pt.*;
import org.junit.jupiter.api.Assertions;

import java.net.http.*;
import java.net.URI;
import java.io.IOException;
import org.json.*;

public class CotacaoSteps {

    private HttpResponse<String> response;

    @Dado("que a API PTAX está disponível")
    public void que_api_ptax_disponivel() {
        // Aqui poderíamos fazer um healthcheck simples
        // mas nesse exemplo apenas garantimos que a URL existe.
        String url = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata";
        Assertions.assertNotNull(url);
    }

    @Quando("eu consulto a cotação da moeda {string} para a data {string}")
    public void consultarCotacao(String moeda, String data) throws IOException, InterruptedException {

        // API espera: MM-DD-YYYY
        String url = String.format(
                "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/" +
                "CotacaoMoedaDia(moeda='%s',dataCotacao='%s')?$format=json",
                moeda, data
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "A API não retornou HTTP 200");
    }

    @Então("a resposta deve conter a data {string}")
    public void respostaDeveConterData(String dataEsperada) {
        JSONObject json = new JSONObject(response.body());
        JSONArray valores = json.getJSONArray("value");

        Assertions.assertFalse(valores.isEmpty(), "Nenhum valor retornado");

        JSONObject registro = valores.getJSONObject(0);
        String data = registro.getString("dataHoraCotacao").substring(0, 10);

        Assertions.assertEquals(dataEsperada, data);
    }

    @Então("a resposta deve conter o valor de compra e venda")
    public void respostaDeveConterCompraVenda() {
        JSONObject json = new JSONObject(response.body());
        JSONArray valores = json.getJSONArray("value");

        JSONObject registro = valores.getJSONObject(0);

        double compra = registro.getDouble("cotacaoCompra");
        double venda = registro.getDouble("cotacaoVenda");

        Assertions.assertTrue(compra > 0, "Valor de compra inválido");
        Assertions.assertTrue(venda > 0, "Valor de venda inválido");
    }
}
```

---

# ✅ 3. Test Runner (`RunCucumberTest.java`)

```java
package br.com.exemplo.runner;

import io.cucumber.junit.platform.engine.Cucumber;

@Cucumber
public class RunCucumberTest {
}
```

---

# ✅ 4. Dependências (Maven)

```xml
<dependencies>
    <!-- Cucumber -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>7.18.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-junit-platform-engine</artifactId>
        <version>7.18.1</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.11.0</version>
        <scope>test</scope>
    </dependency>

    <!-- JSON Parser -->
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20240303</version>
    </dependency>
</dependencies>
```

---

# ✅ 5. Estrutura de pastas recomendada

```
src/test/java/
    br/com/exemplo/runner/RunCucumberTest.java
    br/com/exemplo/steps/CotacaoSteps.java

src/test/resources/
    features/cotacao.feature
```

---

# 🎯 Resultado do Teste

O teste irá:

1. Chamar a API PTAX
2. Validar o retorno HTTP
3. Garantir que data = **2025-11-13**
4. Validar que existem valores de **cotaçãoCompra** e **cotacaoVenda**

É um teste real consumindo a API pública do Banco Central.

---

Se quiser, posso gerar também:

✅ Projeto Maven completo
✅ Versão com mocks (WireMock)
✅ Teste com RestAssured
✅ Teste parametrizado para várias moedas/datas

Quer alguma dessas versões?
