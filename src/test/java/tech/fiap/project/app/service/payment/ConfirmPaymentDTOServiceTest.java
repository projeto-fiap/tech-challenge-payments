package tech.fiap.project.app.service.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.entity.Receipt;
import tech.fiap.project.domain.usecase.order.UpdateOrderUseCase;
import tech.fiap.project.domain.usecase.payment.RetrievePaymentUseCase;
import tech.fiap.project.infra.service.GenerateReceipt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ConfirmPaymentDTOServiceTest {

	@InjectMocks
	private ConfirmPaymentDTOService confirmPaymentDTOService;

	@Mock
	private GenerateReceipt generateReceipt;

	@Mock
	private RetrievePaymentUseCase retrievePaymentUseCase;

	@Mock
	private UpdateOrderUseCase updateOrderUseCase;

	private Payment payment;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		Order order = new Order();
		payment = new Payment(1L, LocalDateTime.now(), "PIX", BigDecimal.valueOf(100.0), Currency.getInstance("BRL"),
				order, tech.fiap.project.app.dto.StatePayment.AWAITING);
	}

	@Test
	void confirmPayment_shouldReturnReceipt_whenPaymentExists() {
		Receipt receipt = new Receipt("1", null);
		when(retrievePaymentUseCase.findById(1L)).thenReturn(Optional.of(payment));
		when(generateReceipt.confirmPayment(payment)).thenReturn(receipt);
		when(updateOrderUseCase.updateOrder(anyLong())).thenReturn(Optional.of(new Order()));
		Receipt result = confirmPaymentDTOService.confirmPayment(1L);

		assertNotNull(result);
		assertEquals("1", result.getId());
		verify(retrievePaymentUseCase, times(1)).findById(1L);
		verify(generateReceipt, times(1)).confirmPayment(payment);
	}

}
