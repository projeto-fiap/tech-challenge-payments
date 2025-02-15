package tech.fiap.project.domain.usecase.impl.order;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import tech.fiap.project.domain.entity.Order;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UpdateOrderUseCaseImplTest {

    @Mock
    private RestTemplate restTemplateOrder;

    @Mock
    private RestTemplate restTemplateKeycloak;

    @InjectMocks
    private UpdateOrderUseCaseImpl updateOrderUseCase;

    private final String orderBaseUrl = "http://order-service";
    private final String keycloakBaseUrl = "http://keycloak-service";
    private final String clientId = "client-id";
    private final String clientSecret = "client-secret";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateOrderUseCase = new UpdateOrderUseCaseImpl(restTemplateOrder, restTemplateKeycloak, orderBaseUrl, keycloakBaseUrl, clientId, clientSecret);
    }

    @Test
    void testUpdateOrder_Success() {
        // Arrange
        Long orderId = 1L;
        String accessToken = "mock-access-token";
        Order mockOrder = new Order();
        mockOrder.setId(orderId);

        // Mock do Keycloak response
        ObjectNode tokenNode = mock(ObjectNode.class);
        when(tokenNode.get("access_token")).thenReturn(mock(com.fasterxml.jackson.databind.JsonNode.class));
        when(tokenNode.get("access_token").asText()).thenReturn(accessToken);

        ResponseEntity<ObjectNode> keycloakResponse = new ResponseEntity<>(tokenNode, HttpStatus.OK);

        // Mock do Order response
        ResponseEntity<Order> orderResponse = new ResponseEntity<>(mockOrder, HttpStatus.OK);

        // Configuração dos mocks
        when(restTemplateKeycloak.exchange(
                eq(keycloakBaseUrl + "/realms/master/protocol/openid-connect/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ObjectNode.class))
        ).thenReturn(keycloakResponse);

        when(restTemplateOrder.exchange(
                eq(orderBaseUrl + "/api/v1/orders/checkout/" + orderId),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Order.class))
        ).thenReturn(orderResponse);

        // Act
        Optional<Order> result = updateOrderUseCase.updateOrder(orderId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().getId());
    }

    @Test
    void testUpdateOrder_Failure() {
        // Arrange
        Long orderId = 1L;
        String accessToken = "mock-access-token";

        // Mock do Keycloak response
        ObjectNode tokenNode = mock(ObjectNode.class);
        when(tokenNode.get("access_token")).thenReturn(mock(com.fasterxml.jackson.databind.JsonNode.class));
        when(tokenNode.get("access_token").asText()).thenReturn(accessToken);

        ResponseEntity<ObjectNode> keycloakResponse = new ResponseEntity<>(tokenNode, HttpStatus.OK);

        // Mock do Order response com erro
        ResponseEntity<Order> orderResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // Configuração dos mocks
        when(restTemplateKeycloak.exchange(
                eq(keycloakBaseUrl + "/realms/master/protocol/openid-connect/token"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ObjectNode.class))
        ).thenReturn(keycloakResponse);

        when(restTemplateOrder.exchange(
                eq(orderBaseUrl + "/api/v1/orders/checkout/" + orderId),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Order.class))
        ).thenReturn(orderResponse);

        // Act
        Optional<Order> result = updateOrderUseCase.updateOrder(orderId);

        // Assert
        assertFalse(result.isPresent());
    }


}