package tech.fiap.project.infra.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tech.fiap.project.app.dto.ItemMercadoLivreDTO;
import tech.fiap.project.app.dto.PaymentResponseDTO;
import tech.fiap.project.domain.entity.Item;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.usecase.impl.order.CalculateTotalOrderUseCaseImpl;
import tech.fiap.project.infra.configuration.MercadoPagoConstants;
import tech.fiap.project.infra.configuration.MercadoPagoProperties;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatePaymentUrlUseCaseMercadoPagoServiceTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private MercadoPagoProperties mercadoPagoProperties;

	@Mock
	private CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase;

	@InjectMocks
	private CreatePaymentUrlUseCaseMercadoPagoService service;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(mercadoPagoProperties.getAccessToken()).thenReturn("mockAccessToken");
		when(mercadoPagoProperties.getUserId()).thenReturn("mockUserId");
		when(mercadoPagoProperties.getPos()).thenReturn("mockPos");
	}

	@Test
	void testExecuteSuccess() {
		Order order = new Order();
		order.setId(1L);
		Item item = new Item(1L, "Item 1", BigDecimal.TEN, BigDecimal.ONE, "unit", null, "description", null);
		order.setItems(Collections.singletonList(item));
		when(calculateTotalOrderUseCase.execute(order.getItems())).thenReturn(BigDecimal.TEN);

		PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
		paymentResponseDTO.setQrData("mockQrData");

		ResponseEntity<PaymentResponseDTO> responseEntity = ResponseEntity.ok(paymentResponseDTO);
		when(restTemplate.exchange(any(RequestEntity.class), eq(PaymentResponseDTO.class))).thenReturn(responseEntity);

		String result = service.execute(order);

		assertEquals("mockQrData", result);
		verify(restTemplate, times(1)).exchange(any(RequestEntity.class), eq(PaymentResponseDTO.class));
	}

	@Test
	void testBuildDescription() {
		// Act
		String description = service.buildDescription(123L);

		// Assert
		assertEquals("Pagamento do pedido 123", description);
	}

	@Test
	void testBuildItems() {
		Item item1 = new Item(1L, "Item 1", BigDecimal.TEN, BigDecimal.ONE, "unit", null, "description 1", null);
		Item item2 = new Item(2L, "Item 2", BigDecimal.valueOf(20), BigDecimal.valueOf(2), "unit", null,
				"description 2", null);

		List<Item> items = Arrays.asList(item1, item2);

		List<ItemMercadoLivreDTO> itemMercadoLivreDTOS = service.buildItems(items);

		assertEquals(2, itemMercadoLivreDTOS.size());
		assertEquals("Item 1", itemMercadoLivreDTOS.get(0).getTitle());
		assertEquals(BigDecimal.valueOf(40), itemMercadoLivreDTOS.get(1).getTotalAmount());
	}

	@Test
	void testBuildItemsWithNull() {
		// Act
		List<ItemMercadoLivreDTO> itemMercadoLivreDTOS = service.buildItems(null);

		// Assert
		assertNotNull(itemMercadoLivreDTOS);
		assertTrue(itemMercadoLivreDTOS.isEmpty());
	}

	@Test
	void testGetHttpHeaders() {
		// Act
		HttpHeaders headers = service.getHttpHeaders();

		// Assert
		assertNotNull(headers);
		assertEquals("Bearer  mockAccessToken", headers.getFirst("Authorization"));
	}

	@Test
	void testBuildBaseUrl() {
		// Act
		String baseUrl = service.buildBaseUrl();

		// Assert
		assertEquals(String.format(MercadoPagoConstants.BASE_PAYMENT_METHOD, "mockUserId", "mockPos"), baseUrl);
	}

}
