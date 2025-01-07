package tech.fiap.project.infra.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GenerateQrCodeExceptionTest {

	@Test
	void testGenerateQrCodeException() {
		String errorMessage = "Error generating QR code";
		GenerateQrCodeException exception = new GenerateQrCodeException(errorMessage);

		assertEquals("Error ao gerar qr code: Error generating QR code", exception.getMessage());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatusCode());
		assertNull(exception.getCause());
	}

}