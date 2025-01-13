
package tech.fiap.project.infra.configuration.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KeycloakJwtAuthenticationConverterTest {

	private KeycloakJwtAuthenticationConverter converter;

	@BeforeEach
	void setUp() {
		ObjectMapper objectMapper = new ObjectMapper();
		converter = new KeycloakJwtAuthenticationConverter(objectMapper);
	}

	@Test
	void testConvert() {
		Jwt jwt = mock(Jwt.class);
		when(jwt.getClaim("realm_access")).thenReturn(Map.of("roles", Set.of("admin", "user")));
		when(jwt.getClaim("resource_access")).thenReturn(
				Map.of("service_a", Map.of("roles", Set.of("viewer")), "service_b", Map.of("roles", Set.of("editor"))));

		JwtAuthenticationToken token = (JwtAuthenticationToken) converter.convert(jwt);

		Collection<GrantedAuthority> authorities = token.getAuthorities();
		Set<String> authorityNames = authorities.stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());

		Set<String> expectedAuthorities = Set.of("ROLE_ADMIN", "ROLE_USER", "ROLE_SERVICE_A_VIEWER",
				"ROLE_SERVICE_B_EDITOR");

		assertEquals(expectedAuthorities, authorityNames);
	}

	@Test
	void testExtractRealmRoles() {
		Jwt jwt = mock(Jwt.class);
		when(jwt.getClaim("realm_access")).thenReturn(Map.of("roles", Set.of("admin", "user")));

		Set<String> roles = converter.getRealmRoles(jwt);

		Set<String> expectedRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
		assertEquals(expectedRoles, roles);
	}

	@Test
	void testExtractResourceRoles() {
		Jwt jwt = mock(Jwt.class);
		when(jwt.getClaim("resource_access")).thenReturn(
				Map.of("service_a", Map.of("roles", Set.of("viewer")), "service_b", Map.of("roles", Set.of("editor"))));

		Set<String> roles = converter.getResourceRoles(jwt);

		Set<String> expectedRoles = Set.of("ROLE_SERVICE_A_VIEWER", "ROLE_SERVICE_B_EDITOR");
		assertEquals(expectedRoles, roles);
	}

	@Test
	void testCreateRole() {
		String role = converter.createRole("service_a", "viewer");
		assertEquals("ROLE_SERVICE_A_VIEWER", role);
	}

}