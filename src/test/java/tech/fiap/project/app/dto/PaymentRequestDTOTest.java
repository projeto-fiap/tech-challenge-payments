package tech.fiap.project.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentRequestDTOTest {

	private PaymentRequestDTO paymentRequestDTO;

	@BeforeEach
	void setUp() {
		// Cria instâncias dos objetos aninhados
		CashOutDTO cashOutDTO = new CashOutDTO(100);
		ItemMercadoLivreDTO item1 = new ItemMercadoLivreDTO("Item 1", "FOOD", "Item", "", BigDecimal.valueOf(50.00), 1,
				"unit", BigDecimal.ONE);
		ItemMercadoLivreDTO item2 = new ItemMercadoLivreDTO("Item 2", "FOOD", "Item", "", BigDecimal.valueOf(75.00), 1,
				"unit", BigDecimal.ONE);

		// Cria o DTO de PaymentRequest
		paymentRequestDTO = new PaymentRequestDTO(cashOutDTO, "Payment description", "EXTERNAL-REF-123",
				Arrays.asList(item1, item2), "http://example.com/notification", "Payment title",
				BigDecimal.valueOf(125.00));
	}

	@Test
	void testGettersAndSetters() {
		// Verifica se os valores definidos são corretamente recuperados pelos getters
		assertEquals(100, paymentRequestDTO.getCashOut().getAmount());
		assertEquals("Payment description", paymentRequestDTO.getDescription());
		assertEquals("EXTERNAL-REF-123", paymentRequestDTO.getExternalReference());
		assertEquals(2, paymentRequestDTO.getItems().size());
		assertEquals("Item 1", paymentRequestDTO.getItems().get(0).getSkuNumber());
		assertEquals("http://example.com/notification", paymentRequestDTO.getNotificationUrl());
		assertEquals("Payment title", paymentRequestDTO.getTitle());
		assertEquals(BigDecimal.valueOf(125.00), paymentRequestDTO.getTotalAmount());
	}

	@Test
	void testConstructor() {
		// Verifica se o DTO foi criado corretamente com o construtor
		CashOutDTO cashOutDTO = new CashOutDTO(150);
		ItemMercadoLivreDTO item1 = new ItemMercadoLivreDTO("Item A", "FOOD", "Item", "", BigDecimal.valueOf(200.00), 1,
				"unit", BigDecimal.ONE);
		PaymentRequestDTO dto = new PaymentRequestDTO(cashOutDTO, "Another description", "EXTERNAL-REF-456",
				Arrays.asList(item1), "http://example.com/notify", "New Payment", BigDecimal.valueOf(200.00));

		assertEquals(150, dto.getCashOut().getAmount());
		assertEquals("Another description", dto.getDescription());
		assertEquals("EXTERNAL-REF-456", dto.getExternalReference());
		assertEquals(1, dto.getItems().size());
		assertEquals("Item A", dto.getItems().get(0).getSkuNumber());
	}

	@Test
	void testJsonPropertyAnnotations() {
		// Verifica se as anotações @JsonProperty funcionam corretamente para o mapeamento
		// JSON
		assertEquals("cash_out",
				paymentRequestDTO.getClass().getDeclaredFields()[0].getAnnotation(JsonProperty.class).value());
		assertEquals("external_reference",
				paymentRequestDTO.getClass().getDeclaredFields()[2].getAnnotation(JsonProperty.class).value());
		assertEquals("notification_url",
				paymentRequestDTO.getClass().getDeclaredFields()[4].getAnnotation(JsonProperty.class).value());
		assertEquals("total_amount",
				paymentRequestDTO.getClass().getDeclaredFields()[6].getAnnotation(JsonProperty.class).value());
	}

}
