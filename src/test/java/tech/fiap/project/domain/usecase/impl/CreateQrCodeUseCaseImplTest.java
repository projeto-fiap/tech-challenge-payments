package tech.fiap.project.domain.usecase.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.usecase.CreatePaymentUrlUseCase;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateQrCodeUseCaseImplTest {

	private CreatePaymentUrlUseCase createPaymentUrlUseCase;
	private GenerateQrCodeUseCaseImpl generateQrCodeUseCaseImpl;
	private CreateQrCodeUseCaseImpl createQrCodeUseCase;

	@BeforeEach
	void setUp() {
		// Mock das dependências
		createPaymentUrlUseCase = mock(CreatePaymentUrlUseCase.class);
		generateQrCodeUseCaseImpl = mock(GenerateQrCodeUseCaseImpl.class);

		// Classe sob teste
		createQrCodeUseCase = new CreateQrCodeUseCaseImpl(createPaymentUrlUseCase, generateQrCodeUseCaseImpl);
	}

	@Test
	void testExecute_ShouldReturnBufferedImage() {
		// Configura os mocks
		Order mockOrder = new Order();
		String mockPaymentUrl = "http://mock-payment-url.com";
		BufferedImage mockBufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

		when(createPaymentUrlUseCase.execute(any(Order.class))).thenReturn(mockPaymentUrl);
		when(generateQrCodeUseCaseImpl.execute(mockPaymentUrl)).thenReturn(mockBufferedImage);

		// Executa o método
		BufferedImage result = createQrCodeUseCase.execute(mockOrder);

		// Verifica os resultados
		assertNotNull(result, "O resultado não deve ser nulo");
		Mockito.verify(createPaymentUrlUseCase).execute(mockOrder);
		Mockito.verify(generateQrCodeUseCaseImpl).execute(mockPaymentUrl);
	}

	@Test
	void testExecute_ShouldHandleNullPaymentUrl() {
		// Configura os mocks
		Order mockOrder = new Order();
		when(createPaymentUrlUseCase.execute(any(Order.class))).thenReturn(null);

		// Executa o método
		BufferedImage result = createQrCodeUseCase.execute(mockOrder);

		// Verifica os resultados
		assertNull(result, "O resultado não deve ser nulo, mesmo com URL de pagamento nula");
		Mockito.verify(createPaymentUrlUseCase).execute(mockOrder);
	}
}
