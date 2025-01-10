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
		http.authorizeHttpRequests(authorize -> {

			authorize.requestMatchers(HttpMethod.POST, "/api/v1/payments/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/v1/payments/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/v1/receipts/**").permitAll();
			// .requestMatchers("/swagger-ui.html", "/swagger-ui/**",
			// "/swagger-resources/**", "/v3/api-docs/**",
			// "/webjars/**")
			// .authenticated();

		}).csrf(AbstractHttpConfigurer::disable).httpBasic(withDefaults());
		return http.build();
	}

}