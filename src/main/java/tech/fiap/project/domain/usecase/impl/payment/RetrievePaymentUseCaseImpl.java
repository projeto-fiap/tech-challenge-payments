package tech.fiap.project.domain.usecase.impl.payment;

import tech.fiap.project.domain.dataprovider.OrderDataProvider;
import tech.fiap.project.domain.dataprovider.PaymentDataProvider;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.usecase.payment.RetrievePaymentUseCase;

import java.util.List;
import java.util.Optional;

public class RetrievePaymentUseCaseImpl implements RetrievePaymentUseCase {

	private final PaymentDataProvider paymentDataProvider;

	private final OrderDataProvider orderDataProvider;

	public RetrievePaymentUseCaseImpl(PaymentDataProvider paymentDataProvider, OrderDataProvider orderDataProvider) {
		this.paymentDataProvider = paymentDataProvider;
		this.orderDataProvider = orderDataProvider;
	}

	@Override
	public List<Payment> findAll() {
		return paymentDataProvider.retrieveAll();
	}

	@Override
	public Optional<Payment> findById(Long id) {
		Order order = orderDataProvider.retrieveByPaymentId(id);
		Optional<Payment> payment = paymentDataProvider.retrieveById(id);
		payment.ifPresent(value -> value.setOrder(order));
		return payment;
	}

}
