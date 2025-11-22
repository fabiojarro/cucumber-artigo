package gemini;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

// Classe que armazena o estado entre os passos do cenário
public class CotacaoSteps {

    // Constante da URL base da API PTAX - Olinda
    private static final String BASE_URL = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata";
    // Variável para armazenar a resposta da API
    private Response response;

    /**
     * Implementação do passo Given para garantir que a API está acessível.
     */
    @Given("que a API PTAX está disponível")
    public void que_a_api_ptax_esta_disponivel() {
        RestAssured.baseURI = BASE_URL;
        // O setup do RestAssured é suficiente para validar a disponibilidade.
        System.out.println("Base URL da API PTAX definida: " + BASE_URL);
    }

    /**
     * Implementação do passo When para realizar a consulta à API.
     * Constrói a URL do endpoint CotacaoMoedaDia com os parâmetros de moeda e data.
     * @param moeda O código da moeda (ex: "USD")
     * @param dataCotacao A data no formato "MM-DD-YYYY" (ex: "11-13-2025")
     */
    @When("eu consulto a cotação da moeda {string} para a data {string}")
    public void eu_consulto_a_cotacao_da_moeda_para_a_data(String moeda, String dataCotacao) {
        // OData exige aspas simples na string de data, ex: '11-13-2025'
//        String endpoint = String.format("/CotacaoMoedaDia(moeda='@moeda',dataCotacao='@dataCotacao')?@moeda='%s'&@dataCotacao='%s'",
//                moeda, dataCotacao);

        String endpoint = String.format(
                "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/" +
                        "CotacaoMoedaDia(moeda='%s',dataCotacao='%s')?$format=json",
                moeda, dataCotacao
        );
        System.out.println("Consultando endpoint: " + BASE_URL + endpoint);

        response = RestAssured.given()
                .when()
                .get(endpoint)
                .then()
                .log().ifError() // Loga o erro se houver
                .extract().response();

        // Validação inicial do status code
        Assertions.assertEquals(200, response.getStatusCode(),
                "O Status Code esperado era 200 (OK), mas retornou: " + response.getStatusCode());
    }

    /**
     * Implementação do passo Then para validar a data na resposta.
     * O BCB retorna a data no formato 'YYYY-MM-DD' na resposta.
     * @param dataEsperada A data esperada no formato 'YYYY-MM-DD' (ex: "2025-11-13")
     */
    @Then("a resposta deve conter a data {string}")
    public void a_resposta_deve_conter_a_data(String dataEsperada) {
        // O BCB retorna os resultados dentro de um array 'value'.
        // Precisamos acessar o primeiro elemento do array.
        List<Map<String, Object>> cotacoes = response.jsonPath().getList("value");

        // Verifica se há resultados
        Assertions.assertFalse(cotacoes.isEmpty(), "A resposta não contém nenhuma cotação para a data especificada.");

        // Extrai a data do primeiro resultado
        String dataCotacao = (String) cotacoes.get(0).get("dataHoraCotacao");

        // Formata a data de 'dd/mm/yyyy hh:mm:ss' para 'yyyy-mm-dd' para comparação
        // A data vem no formato "13/11/2025 13:00:00" e precisamos extrair "2025-11-13"
        String[] partesDataHora = dataCotacao.split(" ");
        String dataPartes = partesDataHora[0];
        Assertions.assertEquals(dataEsperada, dataPartes,
                "A data retornada na cotação não corresponde à data esperada.");
    }

    /**
     * Implementação do passo Then para validar a presença de valores de compra e venda.
     */
    @Then("a resposta deve conter o valor de compra e venda")
    public void a_resposta_deve_conter_o_valor_de_compra_e_venda() {
        List<Map<String, Object>> cotacoes = response.jsonPath().getList("value");

        // Se o passo anterior já validou que há resultados, podemos assumir o primeiro
        Map<String, Object> cotacao = cotacoes.get(0);

        // Verifica se os campos existem e se não são nulos
        Assertions.assertNotNull(cotacao.get("cotacaoCompra"), "O campo 'cotacaoCompra' não foi encontrado ou é nulo.");
        Assertions.assertNotNull(cotacao.get("cotacaoVenda"), "O campo 'cotacaoVenda' não foi encontrado ou é nulo.");

        // Opcional: Verifica se são valores numéricos (Double/Float, pois a API retorna números)
        Assertions.assertTrue(cotacao.get("cotacaoCompra") instanceof Number, "'cotacaoCompra' não é um valor numérico.");
        Assertions.assertTrue(cotacao.get("cotacaoVenda") instanceof Number, "'cotacaoVenda' não é um valor numérico.");

        System.out.println("Cotação Compra: " + cotacao.get("cotacaoCompra"));
        System.out.println("Cotação Venda: " + cotacao.get("cotacaoVenda"));
    }
}