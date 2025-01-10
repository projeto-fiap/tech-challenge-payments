package tech.fiap.project.domain.usecase;

import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.entity.Receipt;

public interface GenerateReceiptUseCase {

	Receipt confirmPayment(Payment payment);

}
