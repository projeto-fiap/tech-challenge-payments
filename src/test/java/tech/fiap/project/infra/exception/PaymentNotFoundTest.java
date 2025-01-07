package tech.fiap.project.infra.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentNotFoundTest {

	@Test
	void testPaymentNotFoundException() {
		String paymentId = "12345";
		PaymentNotFound exception = new PaymentNotFound(paymentId);

		assertEquals("Pagamento 12345 não encontrado", exception.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatusCode());
		assertNull(exception.getCause());
	}

}