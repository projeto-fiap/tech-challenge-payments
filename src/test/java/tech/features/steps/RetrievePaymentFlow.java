package tech.features.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.fiap.project.app.dto.PaymentDTO;
import tech.fiap.project.app.dto.StatePayment;
import tech.fiap.project.app.service.payment.RetrievePaymentService;
import tech.fiap.project.infra.entity.PaymentEntity;
import tech.fiap.project.infra.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RetrievePaymentFlow {

    @Autowired
    private RetrievePaymentService retrievePaymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    private List<PaymentDTO> retrievedPayments;

    @Given("there are payments in the system")
    public void thereArePaymentsInTheSystem() {
        // Cria e salva pagamentos de teste no banco de dados
        PaymentEntity payment1 = new PaymentEntity();
        payment1.setPaymentDate(LocalDateTime.now());
        payment1.setPaymentMethod("Credit Card");
        payment1.setAmount(BigDecimal.valueOf(100.50));
        payment1.setCurrency(Currency.getInstance("USD"));
        payment1.setState(StatePayment.ACCEPTED);

        PaymentEntity payment2 = new PaymentEntity();
        payment2.setPaymentDate(LocalDateTime.now());
        payment2.setPaymentMethod("PayPal");
        payment2.setAmount(BigDecimal.valueOf(200.75));
        payment2.setCurrency(Currency.getInstance("USD"));
        payment2.setState(StatePayment.AWAITING);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
    }

    @When("I request to retrieve all payments")
    public void iRequestToRetrieveAllPayments() {
        retrievedPayments = retrievePaymentService.findAll();
    }

    @Then("I should receive a list of all payments")
    public void iShouldReceiveAListOfAllPayments() {
        assertNotNull(retrievedPayments, "A lista de pagamentos não deve ser nula");
        assertFalse(retrievedPayments.isEmpty(), "A lista de pagamentos não deve estar vazia");

        // Verifica se os pagamentos recuperados correspondem aos esperados
        assertFalse(retrievedPayments.stream()
                        .noneMatch(payment -> "Credit Card".equals(payment.getPaymentMethod())),
                "O método de pagamento 'Credit Card' não foi encontrado");

        assertFalse(retrievedPayments.stream()
                        .noneMatch(payment -> "PayPal".equals(payment.getPaymentMethod())),
                "O método de pagamento 'PayPal' não foi encontrado");
    }
}