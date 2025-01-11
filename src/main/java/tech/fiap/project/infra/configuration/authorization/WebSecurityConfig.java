package tech.fiap.project.infra.configuration.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize ->
			authorize.requestMatchers(HttpMethod.POST, "/api/v1/payments/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/v1/payments/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/v1/receipts/**").permitAll()
					// Definindo as URLs para serem protegidas com autenticação OAuth2
					.requestMatchers("/api/v1/secure/**").authenticated().anyRequest().denyAll() // Proibindo
																									// outras
																									// requisições
																									// não
																									// autenticadas.

		)			// Desabilitando CSRF para permitir chamadas de APIs de outros servidores
				.csrf(AbstractHttpConfigurer::disable).oauth2Login(withDefaults()); // Configura
																					// o
																					// login
																					// OAuth2

		return http.build();
	}

}
