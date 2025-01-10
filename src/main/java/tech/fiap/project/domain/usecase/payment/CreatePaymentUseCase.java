package tech.fiap.project.domain.usecase.payment;

import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.PaymentQrcode;

public interface CreatePaymentUseCase {

	PaymentQrcode execute(Order order);

}
