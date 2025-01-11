package tech.fiap.project.domain.usecase.impl.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.fiap.project.app.dto.StatePayment;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.entity.PaymentQrcode;
import tech.fiap.project.domain.usecase.CreateQrCodeUseCase;
import tech.fiap.project.domain.usecase.payment.CreatePayment;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CreatePaymentUseCaseImplTest {

	@InjectMocks
	private CreatePaymentUseCaseImpl createPaymentUseCase;

	@Mock
	private CreateQrCodeUseCase generateQrCode;

	@Mock
	private CreatePayment createPayment;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void execute_shouldReturnPaymentQrcodeSuccessfully() {
		Order mockOrder = new Order();
		Payment mockPayment = new Payment(1L, LocalDateTime.now(), "PIX", BigDecimal.ONE, Currency.getInstance("BRL"),
				new Order(), StatePayment.ACCEPTED);
		BufferedImage mockQrCode = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

		when(createPayment.execute(mockOrder)).thenReturn(mockPayment);
		when(generateQrCode.execute(mockOrder)).thenReturn(mockQrCode);

		PaymentQrcode result = createPaymentUseCase.execute(mockOrder);

		assertEquals(mockPayment, result.getPayment());
		assertEquals(mockQrCode, result.getQrcode());
		verify(createPayment, times(1)).execute(mockOrder);
		verify(generateQrCode, times(1)).execute(mockOrder);
	}

	@Test
	void execute_shouldThrowExceptionWhenCreatePaymentFails() {
		Order mockOrder = new Order();
		when(createPayment.execute(mockOrder)).thenThrow(new RuntimeException("Failed to create payment"));

		try {
			createPaymentUseCase.execute(mockOrder);
		}
		catch (RuntimeException e) {
			assertEquals("Failed to create payment", e.getMessage());
		}

		verify(createPayment, times(1)).execute(mockOrder);
		verify(generateQrCode, times(0)).execute(mockOrder);
	}

	@Test
	void execute_shouldThrowExceptionWhenGenerateQrCodeFails() {
		Order mockOrder = new Order();
		Payment mockPayment = new Payment(1L, LocalDateTime.now(), "PIX", BigDecimal.ONE, Currency.getInstance("BRL"),
				new Order(), StatePayment.ACCEPTED);
		when(createPayment.execute(mockOrder)).thenReturn(mockPayment);
		when(generateQrCode.execute(mockOrder)).thenThrow(new RuntimeException("Failed to generate QR Code"));

		try {
			createPaymentUseCase.execute(mockOrder);
		}
		catch (RuntimeException e) {
			assertEquals("Failed to generate QR Code", e.getMessage());
		}

		verify(createPayment, times(1)).execute(mockOrder);
		verify(generateQrCode, times(1)).execute(mockOrder);
	}

}
