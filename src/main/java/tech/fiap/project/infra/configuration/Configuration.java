package tech.fiap.project.infra.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;
import tech.fiap.project.domain.dataprovider.OrderDataProvider;
import tech.fiap.project.domain.dataprovider.PaymentDataProvider;
import tech.fiap.project.domain.dataprovider.ReceiptDataProvider;
import tech.fiap.project.domain.usecase.CreatePaymentUrlUseCase;
import tech.fiap.project.domain.usecase.CreateQrCodeUseCase;
import tech.fiap.project.domain.usecase.impl.CreateQrCodeUseCaseImpl;
import tech.fiap.project.domain.usecase.impl.GenerateQrCodeUseCaseImpl;
import tech.fiap.project.domain.usecase.impl.order.CalculateTotalOrderUseCaseImpl;
import tech.fiap.project.domain.usecase.impl.order.UpdateOrderUseCaseImpl;
import tech.fiap.project.domain.usecase.impl.payment.CreatePaymentImpl;
import tech.fiap.project.domain.usecase.impl.payment.CreatePaymentUseCaseImpl;
import tech.fiap.project.domain.usecase.impl.payment.RetrievePaymentUseCaseImpl;
import tech.fiap.project.domain.usecase.impl.receipt.RetrieveReceiptUseCaseImpl;
import tech.fiap.project.domain.usecase.payment.CreatePayment;
import tech.fiap.project.domain.usecase.payment.RetrievePaymentUseCase;
import tech.fiap.project.domain.usecase.receipt.RetrieveReceiptUseCase;

import javax.net.ssl.SSLContext;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@org.springframework.context.annotation.Configuration
@Getter
@ComponentScan("tech.fiap.project")
@Setter
public class Configuration {

	@Value("${tech-challenge.orders.base-url}")
	String ordersBaseUrl;

	@Value("${keycloak.base-url}")
	String keycloakBaseUrl;

	@Value("${tech-challenge.payments.client-id}")
	String paymentsClientId;

	@Value("${tech-challenge.payments.client-secret}")
	String paymentsClientSecret;

	@Value("${mongodb.uri}")
	String mongoUri;

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
		objectMapper.setDateFormat(df);
		objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
		return objectMapper;
	}

	@Bean
	public RestTemplate restTemplateMercadoPago() {
		UriTemplateHandler uriTemplateHandler = new DefaultUriBuilderFactory(MercadoPagoConstants.BASE_URI);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setUriTemplateHandler(uriTemplateHandler);
		return restTemplate;
	}

	@Bean
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}

	@Bean
	public GenerateQrCodeUseCaseImpl generateQrCode() {
		return new GenerateQrCodeUseCaseImpl();
	}

	@Bean
	public CreateQrCodeUseCaseImpl createQrCodeUseCase(CreatePaymentUrlUseCase createPaymentUrlUseCase,
			GenerateQrCodeUseCaseImpl generateQrCode) {
		return new CreateQrCodeUseCaseImpl(createPaymentUrlUseCase, generateQrCode);
	}

	@Bean
	public CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase() {
		return new CalculateTotalOrderUseCaseImpl();
	}

	@Bean
	public RetrievePaymentUseCase retrievePaymentUseCase(OrderDataProvider orderDataProvider,
			PaymentDataProvider paymentDataProvider) {
		return new RetrievePaymentUseCaseImpl(paymentDataProvider, orderDataProvider);
	}

	@Bean
	public CreatePaymentImpl createPayment(OrderDataProvider orderDataProvider, PaymentDataProvider createPayment,
			CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase) {
		return new CreatePaymentImpl(orderDataProvider, createPayment, calculateTotalOrderUseCase);
	}

	@Bean
	public CreatePaymentUseCaseImpl createPaymentUseCase(CreateQrCodeUseCase createQrCodeUseCase,
			CreatePayment createPayment) {
		return new CreatePaymentUseCaseImpl(createQrCodeUseCase, createPayment);
	}

	@Bean
	public RetrieveReceiptUseCase retrieveReceiptUseCase(ReceiptDataProvider receiptDataProvider) {
		return new RetrieveReceiptUseCaseImpl(receiptDataProvider);
	}

	@Bean
	public UpdateOrderUseCaseImpl updateOrderUseCase(RestTemplate restTemplateOrder,
			RestTemplate restTemplateKeycloak) {
		return new UpdateOrderUseCaseImpl(restTemplateOrder, restTemplateKeycloak,ordersBaseUrl, keycloakBaseUrl, paymentsClientId,
				paymentsClientSecret);
	}

	@Bean
	public MongoClientSettings mongoClientSettings() throws Exception {
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, null, null);

		ConnectionString connectionString = new ConnectionString(mongoUri);

		return MongoClientSettings.builder()
				.applyConnectionString(connectionString)
				.applyToSslSettings(builder -> builder.enabled(true).context(sslContext))
				.build();
	}

}
