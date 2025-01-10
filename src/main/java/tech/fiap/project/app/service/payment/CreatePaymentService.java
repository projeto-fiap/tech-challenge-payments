package tech.fiap.project.app.service.payment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.PaymentQrcode;
import tech.fiap.project.domain.usecase.payment.CreatePaymentUseCase;

@Service
@AllArgsConstructor
public class CreatePaymentService {

	private CreatePaymentUseCase createPaymentUseCase;

	public PaymentQrcode execute(Order order) {
		return createPaymentUseCase.execute(order);
	}

}
