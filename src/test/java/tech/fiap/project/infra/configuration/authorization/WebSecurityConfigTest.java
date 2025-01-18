package tech.fiap.project.infra.configuration.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@Import({ WebSecurityConfig.class })
@EnableWebSecurity
class WebSecurityConfigTest {

	private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter = new KeycloakJwtAuthenticationConverter(
			new ObjectMapper());

	ObjectPostProcessor<Object> objectPostProcessor = new ObjectPostProcessor<Object>() {
		@Override
		public <O> O postProcess(O object) {
			return object;
		}
	};

	private HttpSecurity httpSecurity;

	{
		HashMap<Class<?>, Object> sharedObjects = new HashMap<>();
		GenericApplicationContext value = new GenericApplicationContext();
		value.getBeanFactory().registerSingleton("jwtDecoder",
				NimbusJwtDecoder.withJwkSetUri("https://your-auth-server/.well-known/jwks.json").build());
		value.getBeanFactory().registerSingleton("clientRegistrationRepository'", new ClientRegistrationRepository() {
			@Override
			public ClientRegistration findByRegistrationId(String registrationId) {
				return null;
			}
		});
		value.refresh();
		sharedObjects.put(ApplicationContext.class, value);
		httpSecurity = Mockito.spy(new HttpSecurity(objectPostProcessor,
				new AuthenticationManagerBuilder(objectPostProcessor), sharedObjects));
	}

	private WebSecurityConfig webSecurityConfig = new WebSecurityConfig(keycloakJwtAuthenticationConverter);

	@Test
	void filterChain_shouldConfigureHttpSecurity() throws Exception {
		SecurityFilterChain securityFilterChain = webSecurityConfig.filterChain(httpSecurity);
		assertNotNull(securityFilterChain);
		verify(httpSecurity).authorizeHttpRequests(any());
		verify(httpSecurity).oauth2Client(any());
		verify(httpSecurity).oauth2ResourceServer(any());
	}

}
