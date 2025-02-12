package tech.fiap.project.domain.usecase.impl.order;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.usecase.order.UpdateOrderUseCase;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class UpdateOrderUseCaseImpl implements UpdateOrderUseCase {

	private final RestTemplate restTemplateOrder;

	private final RestTemplate restTemplateKeycloak;

	private final String orderBaseUrl;

	private final String clientId;

	private final String keycloakBaseUrl;

	private final String clientSecret;

	public UpdateOrderUseCaseImpl(RestTemplate restTemplateOrder, RestTemplate restTemplateKeycloak, String orderBaseUrl,
                                  String keycloakBaseUrl, String clientId, String clientSecret) {
		this.restTemplateOrder = restTemplateOrder;
		this.restTemplateKeycloak = restTemplateKeycloak;
        this.orderBaseUrl = orderBaseUrl;
        this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.keycloakBaseUrl = keycloakBaseUrl;
	}

	@Override
	public Optional<Order> updateOrder(Long orderId) {
		String accessToken = getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Order> requestEntity = new HttpEntity<>(null, headers);
		log.info("accessToken {}", accessToken);
		log.info("orderId {}", orderId);
	 	ResponseEntity<Order> response = restTemplateOrder.exchange(orderBaseUrl + "/api/v1/orders/checkout/" + orderId, HttpMethod.PUT, requestEntity, Order.class);
		return response.getStatusCode().is2xxSuccessful() ? Optional.ofNullable(response.getBody()) : Optional.empty();
	}

	protected String getAccessToken() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String body = String.format("client_id=%s&client_secret=%s&grant_type=client_credentials", clientId,
				clientSecret);
		HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
		ResponseEntity<ObjectNode> response = restTemplateKeycloak.exchange(
				keycloakBaseUrl + "/realms/master/protocol/openid-connect/token", HttpMethod.POST, requestEntity,
				ObjectNode.class);
		return Objects.requireNonNull(response.getBody()).get("access_token").asText();
	}

}
