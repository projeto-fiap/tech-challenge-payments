package tech.fiap.project.app.service.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.entity.Receipt;
import tech.fiap.project.domain.usecase.payment.RetrievePaymentUseCase;
import tech.fiap.project.infra.exception.PaymentNotFound;
import tech.fiap.project.infra.service.GenerateReceipt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfirmPaymentDTOServiceTest {

	@InjectMocks
	private ConfirmPaymentDTOService confirmPaymentDTOService;

	@Mock
	private GenerateReceipt generateReceipt;

	@Mock
	private RetrievePaymentUseCase retrievePaymentUseCase;

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
		// Arrange
		Receipt receipt = new Receipt("1", null);
		when(retrievePaymentUseCase.findById(1L)).thenReturn(Optional.of(payment));
		when(generateReceipt.confirmPayment(payment)).thenReturn(receipt);

		// Act
		Receipt result = confirmPaymentDTOService.confirmPayment(1L);

		// Assert
		assertNotNull(result);
		assertEquals("1", result.getId());
		verify(retrievePaymentUseCase, times(1)).findById(1L);
		verify(generateReceipt, times(1)).confirmPayment(payment);
	}

	@Test
	void confirmPayment_shouldThrowPaymentNotFound_whenPaymentDoesNotExist() {
		// Arrange
		when(retrievePaymentUseCase.findById(1L)).thenReturn(Optional.empty());

		// Act & Assert
		PaymentNotFound exception = assertThrows(PaymentNotFound.class,
				() -> confirmPaymentDTOService.confirmPayment(1L));
		assertEquals("Pagamento 1 não encontrado", exception.getMessage());
		verify(retrievePaymentUseCase, times(1)).findById(1L);
		verifyNoInteractions(generateReceipt);
	}

}
