package tech.fiap.project.infra.configuration.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;

	public WebSecurityConfig(KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter) {
		this.keycloakJwtAuthenticationConverter = keycloakJwtAuthenticationConverter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated()).oauth2Client(withDefaults())
				.oauth2ResourceServer(
						httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer
								.jwt(jwtConfigurer -> jwtConfigurer
										.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)));
		return http.build();
	}

}