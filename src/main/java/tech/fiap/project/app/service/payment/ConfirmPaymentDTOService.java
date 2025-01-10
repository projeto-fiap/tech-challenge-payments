package tech.fiap.project.app.service.payment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.entity.Receipt;
import tech.fiap.project.domain.usecase.payment.RetrievePaymentUseCase;
import tech.fiap.project.infra.exception.PaymentNotFound;
import tech.fiap.project.infra.service.GenerateReceipt;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ConfirmPaymentDTOService {

	private final GenerateReceipt generateReceipt;
	private final RetrievePaymentUseCase retrievePaymentUseCase;

	public Receipt confirmPayment(Long paymentId) {
		Optional<Payment> payment = retrievePaymentUseCase.findById(paymentId);
        if (payment.isEmpty()) {
            throw new PaymentNotFound(paymentId.toString());
		}
		return generateReceipt.confirmPayment(payment.get());
	}

}
