package tech.fiap.project.domain.usecase.impl.order;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tech.fiap.project.domain.entity.Order;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateOrderUseCaseImplTest {

	private RestTemplate restTemplateOrder;

	private RestTemplate restTemplateKeycloak;

	private UpdateOrderUseCaseImpl updateOrderUseCaseImpl;

	private String keycloakBaseUrl = "http://localhost:8080";

	private String clientId = "testClientId";

	private String orderBaseUrl = "http://localhost:8082";

	private String clientSecret = "testClientSecret";

	@BeforeEach
	void setUp() {
		restTemplateOrder = mock(RestTemplate.class);
		restTemplateKeycloak = mock(RestTemplate.class);
		updateOrderUseCaseImpl = new UpdateOrderUseCaseImpl(restTemplateOrder, restTemplateKeycloak, orderBaseUrl,
				keycloakBaseUrl, clientId, clientSecret);
	}

	@Test
	void getAccessToken_ShouldReturnValidToken() {
		// Mock the keycloak token response
		String accessToken = "mockAccessToken";
		ObjectNode mockTokenResponse = mock(ObjectNode.class);
		when(mockTokenResponse.get("access_token")).thenReturn(new TextNode(accessToken));
		ResponseEntity<ObjectNode> tokenResponseEntity = new ResponseEntity<>(mockTokenResponse, HttpStatus.OK);
		when(restTemplateKeycloak.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ObjectNode.class)))
				.thenReturn(tokenResponseEntity);

		// Call the method
		String token = updateOrderUseCaseImpl.getAccessToken();

		// Verify the result
		assertEquals(accessToken, token);
	}

}
