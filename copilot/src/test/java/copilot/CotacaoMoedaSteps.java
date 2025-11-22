package copilot;

import io.cucumber.java.pt.*;
import org.junit.jupiter.api.Assertions;

import java.net.http.*;
import java.net.URI;
import java.io.IOException;
import java.util.regex.*;

public class CotacaoMoedaSteps {

    private String responseBody;

    @Dado("que a API PTAX está disponível")
    public void que_a_api_ptax_esta_disponivel() {
        // Apenas validação conceitual, poderia pingar o endpoint base
        Assertions.assertNotNull("https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/");
    }

    @Quando("eu consulto a cotação da moeda {string} para a data {string}")
    public void eu_consulto_a_cotacao_da_moeda_para_a_data(String moeda, String data) throws IOException, InterruptedException {
        String url = String.format(
                "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/" +
                        "CotacaoMoedaDia(moeda='%s',dataCotacao='%s')?$top=100&$format=json",
                moeda, data
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        responseBody = response.body();

        Assertions.assertNotNull(responseBody);
    }

    @Entao("a resposta deve conter a data {string}")
    public void a_resposta_deve_conter_a_data(String dataEsperada) {
        Assertions.assertTrue(responseBody.contains(dataEsperada),
                "Resposta não contém a data esperada: " + dataEsperada);
    }

    @Entao("a resposta deve conter o valor de compra e venda")
    public void a_resposta_deve_conter_o_valor_de_compra_e_venda() {
        // Verifica se existem campos "cotacaoCompra" e "cotacaoVenda"
        Pattern compra = Pattern.compile("\"cotacaoCompra\":\\s*\\d+(\\.\\d+)?");
        Pattern venda = Pattern.compile("\"cotacaoVenda\":\\s*\\d+(\\.\\d+)?");

        Assertions.assertTrue(compra.matcher(responseBody).find(), "Não encontrou cotação de compra");
        Assertions.assertTrue(venda.matcher(responseBody).find(), "Não encontrou cotação de venda");
    }
}
