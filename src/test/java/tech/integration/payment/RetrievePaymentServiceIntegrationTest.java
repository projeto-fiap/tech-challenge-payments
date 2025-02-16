package tech.integration.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import tech.fiap.project.app.dto.PaymentDTO;
import tech.fiap.project.app.dto.StatePayment;
import tech.fiap.project.app.service.payment.RetrievePaymentService;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.usecase.payment.RetrievePaymentUseCase;
import tech.fiap.project.infra.configuration.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = { Configuration.class, ServletWebServerFactoryAutoConfiguration.class })
@TestPropertySource(properties = "keycloak.base-url=http://localhost:29000")
@ActiveProfiles("integration-test")
public class RetrievePaymentServiceIntegrationTest {

    @Autowired
    private RetrievePaymentService retrievePaymentService;

    @MockBean
    private RetrievePaymentUseCase retrievePaymentUseCase;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(1L);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod("Credit Card");
        payment.setAmount(BigDecimal.valueOf(100.50));
        payment.setCurrency(Currency.getInstance("USD"));
        payment.setState(StatePayment.ACCEPTED);
    }

    @Test
    void findAll_shouldReturnListOfPaymentDTOs_whenPaymentsExist() {
        when(retrievePaymentUseCase.findAll()).thenReturn(Collections.singletonList(payment));

        List<PaymentDTO> result = retrievePaymentService.findAll();

        assertFalse(result.isEmpty(), "A lista de pagamentos não deve estar vazia");
        assertEquals(1, result.size(), "A lista deve conter exatamente 1 pagamento");

        PaymentDTO paymentDTO = result.get(0);
        assertEquals(payment.getId(), paymentDTO.getId(), "O ID do pagamento deve corresponder");
        assertEquals(payment.getPaymentMethod(), paymentDTO.getPaymentMethod(), "O método de pagamento deve corresponder");
        assertEquals(payment.getAmount(), paymentDTO.getAmount(), "O valor do pagamento deve corresponder");
        assertEquals(payment.getCurrency(), paymentDTO.getCurrency(), "A moeda do pagamento deve corresponder");

        verify(retrievePaymentUseCase, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoPaymentsExist() {
        when(retrievePaymentUseCase.findAll()).thenReturn(Collections.emptyList());

        List<PaymentDTO> result = retrievePaymentService.findAll();

        assertTrue(result.isEmpty(), "A lista de pagamentos deve estar vazia");

        verify(retrievePaymentUseCase, times(1)).findAll();
    }
}