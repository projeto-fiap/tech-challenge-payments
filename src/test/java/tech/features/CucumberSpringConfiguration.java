package tech.features;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import tech.fiap.project.infra.configuration.Configuration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = { Configuration.class, ServletWebServerFactoryAutoConfiguration.class })
@TestPropertySource(properties = "keycloak.base-url=http://localhost:29000")
@ActiveProfiles("integration-test")
public class CucumberSpringConfiguration {

}