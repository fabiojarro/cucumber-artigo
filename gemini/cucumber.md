```java
package br.com.ptax.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/**
 * Classe para executar os testes Cucumber usando JUnit 5 (JUnit Platform).
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("br/com/ptax/features") // Localização dos arquivos .feature
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "br.com.ptax.steps") // Localização das classes de Steps
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/CucumberReport.html")
public class TestRunner {
    // Esta classe não precisa de corpo. As anotações definem a execução do Cucumber.
}


```