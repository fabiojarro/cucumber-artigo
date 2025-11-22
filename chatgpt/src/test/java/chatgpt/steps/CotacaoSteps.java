package chatgpt.steps;

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
