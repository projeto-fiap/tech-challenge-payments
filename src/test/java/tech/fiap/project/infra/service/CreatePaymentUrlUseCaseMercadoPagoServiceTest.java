package tech.fiap.project.infra.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import tech.fiap.project.app.dto.ItemMercadoLivreDTO;
import tech.fiap.project.domain.entity.Item;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.usecase.impl.order.CalculateTotalOrderUseCaseImpl;
import tech.fiap.project.infra.configuration.MercadoPagoProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreatePaymentUrlUseCaseMercadoPagoServiceTest {

	@Mock
	private RestTemplate restTemplateMercadoPago;

	@Mock
	private MercadoPagoProperties mercadoPagoProperties;

	@Mock
	private CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCaseImpl;

	private CreatePaymentUrlUseCaseMercadoPagoService createPaymentUrlUseCaseMercadoPagoService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		createPaymentUrlUseCaseMercadoPagoService = new CreatePaymentUrlUseCaseMercadoPagoService(
				restTemplateMercadoPago, mercadoPagoProperties, calculateTotalOrderUseCaseImpl);
	}

	@Test
	void testBuildItems_shouldReturnCorrectItemsList() {
		Item item1 = new Item(1L, "Item1", BigDecimal.valueOf(50.0), BigDecimal.valueOf(2), "unit", null,
				"Item description", "imageUrl1");
		Item item2 = new Item(2L, "Item2", BigDecimal.valueOf(30.0), BigDecimal.valueOf(1), "unit", null,
				"Another item", "imageUrl2");
		Order order = new Order(1L, LocalDateTime.now(), LocalDateTime.now(), Arrays.asList(item1, item2), null, null,
				BigDecimal.valueOf(150.0));

		List<ItemMercadoLivreDTO> items = createPaymentUrlUseCaseMercadoPagoService.buildItems(order.getItems());

		assertNotNull(items);
		assertEquals(2, items.size());
		ItemMercadoLivreDTO firstItem = items.get(0);
		assertEquals("marketplace", firstItem.getCategory());
		assertEquals("Item description", firstItem.getDescription());
		assertEquals(BigDecimal.valueOf(50.0), firstItem.getUnitPrice());
		assertEquals(2, firstItem.getQuantity());
		assertEquals("unit", firstItem.getUnitMeasure());
		assertEquals(BigDecimal.valueOf(100.0), firstItem.getTotalAmount());
	}

	@Test
	void testBuildItems_shouldReturnEmptyList_whenItemsAreNull() {
		List<ItemMercadoLivreDTO> items = createPaymentUrlUseCaseMercadoPagoService.buildItems(null);
		assertNotNull(items);
		assertTrue(items.isEmpty());
	}

	@Test
	void testBuildDescription_shouldReturnCorrectDescription() {
		Long orderId = 123L;

		String description = createPaymentUrlUseCaseMercadoPagoService.buildDescription(orderId);

		assertEquals("Pagamento do pedido 123", description);
	}

}
