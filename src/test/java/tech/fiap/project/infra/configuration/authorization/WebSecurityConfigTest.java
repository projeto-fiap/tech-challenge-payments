package tech.fiap.project.infra.configuration.authorization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Import(WebSecurityConfig.class)
@EnableWebSecurity
class WebSecurityConfigTest {

	private KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;

	@BeforeEach
	void setUp() {
		keycloakJwtAuthenticationConverter = Mockito.mock(KeycloakJwtAuthenticationConverter.class);
	}

	@Test
	void testJwtAuthenticationConverter_shouldBeCalled() {
		Mockito.verify(keycloakJwtAuthenticationConverter, Mockito.never()).convert(Mockito.any());
	}

}
