package tech.fiap.project.infra.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import tech.fiap.project.domain.dataprovider.PaymentDataProvider;
import tech.fiap.project.domain.usecase.CreatePaymentUrlUseCase;
import tech.fiap.project.domain.usecase.impl.CreateQrCodeUseCaseImpl;
import tech.fiap.project.domain.usecase.impl.GenerateQrCodeUseCaseImpl;
import tech.fiap.project.domain.usecase.impl.order.CalculateTotalOrderUseCaseImpl;
import tech.fiap.project.domain.usecase.impl.payment.RetrievePaymentUseCaseImpl;
import tech.fiap.project.domain.usecase.payment.RetrievePaymentUseCase;

import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ConfigurationTest {

	private final Configuration configuration = new Configuration();

	@Test
	void objectMapper_shouldReturnConfiguredObjectMapper() {
		ObjectMapper objectMapper = configuration.objectMapper();
		assertNotNull(objectMapper);
		DateFormat dateFormat = objectMapper.getDateFormat();
		assertNotNull(dateFormat);
		assert (dateFormat instanceof SimpleDateFormat);
	}

	@Test
	void restTemplateMercadoPago_shouldReturnConfiguredRestTemplate() {
		RestTemplate restTemplate = configuration.restTemplateMercadoPago();
		assertNotNull(restTemplate);
		assertNotNull(restTemplate.getUriTemplateHandler());
	}

	@Test
	void createImageHttpMessageConverter_shouldReturnBufferedImageHttpMessageConverter() {
		HttpMessageConverter<BufferedImage> converter = configuration.createImageHttpMessageConverter();
		assertNotNull(converter);
		assert (converter instanceof BufferedImageHttpMessageConverter);
	}

	@Test
	void generateQrCode_shouldReturnGenerateQrCodeUseCaseImpl() {
		GenerateQrCodeUseCaseImpl generateQrCode = configuration.generateQrCode();
		assertNotNull(generateQrCode);
	}

	@Test
	void createQrCodeUseCase_shouldReturnCreateQrCodeUseCaseImpl() {
		CreatePaymentUrlUseCase createPaymentUrlUseCase = mock(CreatePaymentUrlUseCase.class);
		GenerateQrCodeUseCaseImpl generateQrCode = mock(GenerateQrCodeUseCaseImpl.class);
		CreateQrCodeUseCaseImpl createQrCodeUseCase = configuration.createQrCodeUseCase(createPaymentUrlUseCase,
				generateQrCode);
		assertNotNull(createQrCodeUseCase);
	}

	@Test
	void calculateTotalOrderUseCase_shouldReturnCalculateTotalOrderUseCaseImpl() {
		CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase = configuration.calculateTotalOrderUseCase();
		assertNotNull(calculateTotalOrderUseCase);
	}

	@Test
	void retrievePaymentUseCase_shouldReturnRetrievePaymentUseCaseImpl() {
		PaymentDataProvider paymentDataProvider = mock(PaymentDataProvider.class);
		RetrievePaymentUseCase retrievePaymentUseCase = configuration.retrievePaymentUseCase(paymentDataProvider);
		assertNotNull(retrievePaymentUseCase);
		assert (retrievePaymentUseCase instanceof RetrievePaymentUseCaseImpl);
	}

}