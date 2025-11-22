Testes BDD - Consulta de Cotação de Moeda (PTAX)

Este projeto demonstra a implementação de testes BDD para a API PTAX do Banco Central do Brasil utilizando Java 21, Cucumber 7 e JUnit 5.

Pré-requisitos

Java Development Kit (JDK) 21 ou superior

Apache Maven

Estrutura do Projeto

pom.xml: Contém todas as dependências (Cucumber, JUnit 5, RestAssured).

src/test/resources/br/com/ptax/features/CotacaoMoedaDia.feature: O arquivo Gherkin que descreve o teste.

src/test/java/br/com/ptax/steps/CotacaoSteps.java: A implementação dos passos em Java.

src/test/java/br/com/ptax/runner/TestRunner.java: A classe de execução para o JUnit 5.

Como Executar os Testes

Navegue até o diretório raiz do projeto (onde o arquivo pom.xml está localizado).

Execute o comando Maven para rodar todos os testes (que incluem o TestRunner do Cucumber):

mvn clean test


Saída Esperada

O console exibirá o progresso da execução do Cucumber, indicando que o Cenário foi executado e todos os passos foram passados com sucesso (se a API estiver disponível e a cotação for encontrada).

Relatórios

Após a execução, um relatório HTML será gerado no diretório:
target/cucumber-reports/CucumberReport.html