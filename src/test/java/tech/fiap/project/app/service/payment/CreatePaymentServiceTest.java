package tech.fiap.project.app.service.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.entity.PaymentQrcode;
import tech.fiap.project.domain.usecase.payment.CreatePaymentUseCase;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatePaymentServiceTest {

	private CreatePaymentUseCase createPaymentUseCase;

	private CreatePaymentService createPaymentService;

	@BeforeEach
	void setUp() {
		createPaymentUseCase = Mockito.mock(CreatePaymentUseCase.class);
		createPaymentService = new CreatePaymentService(createPaymentUseCase);
	}

	@Test
	void execute_shouldReturnPaymentQrcode_whenOrderIsValid() {
		Order order = new Order();
		Payment payment = new Payment(1L, null, "PIX", null, null, order, null);
		BufferedImage qrcode = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		PaymentQrcode expectedPaymentQrcode = new PaymentQrcode(payment, qrcode);

		when(createPaymentUseCase.execute(order)).thenReturn(expectedPaymentQrcode);

		PaymentQrcode actualPaymentQrcode = createPaymentService.execute(order);

		assertNotNull(actualPaymentQrcode);
		assertEquals(expectedPaymentQrcode, actualPaymentQrcode);
		verify(createPaymentUseCase, times(1)).execute(order);
	}

	@Test
	void execute_shouldThrowException_whenCreatePaymentUseCaseFails() {
		Order order = new Order();
		when(createPaymentUseCase.execute(order)).thenThrow(new RuntimeException("Error creating payment"));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> createPaymentService.execute(order));
		assertEquals("Error creating payment", exception.getMessage());
		verify(createPaymentUseCase, times(1)).execute(order);
	}

}
