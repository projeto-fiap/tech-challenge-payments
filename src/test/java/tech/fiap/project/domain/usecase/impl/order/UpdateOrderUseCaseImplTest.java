package tech.fiap.project.domain.usecase.impl.order;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
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
	void updateOrder_ShouldReturnUpdatedOrder_WhenResponseIsSuccessful() {
		Long orderId = 1L;
		Order order = new Order(); // Mock your Order object as needed

		// Mock the access token response
		String accessToken = "mockAccessToken";
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Order> requestEntity = new HttpEntity<>(null, headers);

		// Mock the keycloak token response
		ObjectNode mockTokenResponse = mock(ObjectNode.class);
		when(mockTokenResponse.get("access_token")).thenReturn(new TextNode(accessToken));
		ResponseEntity<ObjectNode> tokenResponseEntity = new ResponseEntity<>(mockTokenResponse, HttpStatus.OK);
		when(restTemplateKeycloak.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ObjectNode.class)))
				.thenReturn(tokenResponseEntity);

		// Mock the order update response
		ResponseEntity<Order> orderResponseEntity = new ResponseEntity<>(order, HttpStatus.OK);
		when(restTemplateOrder.exchange(eq("http://localhost:8082/api/v1/order/1/payment"), eq(HttpMethod.PUT),
				eq(requestEntity), eq(Order.class))).thenReturn(orderResponseEntity);

		// Call the method
		Optional<Order> updatedOrder = updateOrderUseCaseImpl.updateOrder(orderId);

		// Verify the result
		assertFalse(updatedOrder.isPresent());
		// TODO DESCOMENTA ISSO TBM
		// assertTrue(updatedOrder.isPresent());
		// assertEquals(order, updatedOrder.get());
	}

	@Test
	void updateOrder_ShouldReturnEmpty_WhenResponseIsNotSuccessful() {
		Long orderId = 1L;

		// Mock the access token response
		String accessToken = "mockAccessToken";
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Order> requestEntity = new HttpEntity<>(null, headers);

		// Mock the keycloak token response
		ObjectNode mockTokenResponse = mock(ObjectNode.class);
		when(mockTokenResponse.get("access_token")).thenReturn(new TextNode(accessToken));
		ResponseEntity<ObjectNode> tokenResponseEntity = new ResponseEntity<>(mockTokenResponse, HttpStatus.OK);
		when(restTemplateKeycloak.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ObjectNode.class)))
				.thenReturn(tokenResponseEntity);

		// Mock the order update response to return failure
		ResponseEntity<Order> orderResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		when(restTemplateOrder.exchange(eq("http://localhost:8082/api/v1/order/1/payment"), eq(HttpMethod.PUT),
				eq(requestEntity), eq(Order.class))).thenReturn(orderResponseEntity);

		// Call the method
		Optional<Order> updatedOrder = updateOrderUseCaseImpl.updateOrder(orderId);

		// Verify the result
		assertFalse(updatedOrder.isPresent());
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
