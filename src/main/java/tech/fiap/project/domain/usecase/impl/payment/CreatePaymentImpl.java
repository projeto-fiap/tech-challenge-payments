package tech.fiap.project.domain.usecase.impl.payment;

import tech.fiap.project.app.dto.StatePayment;
import tech.fiap.project.domain.dataprovider.OrderDataProvider;
import tech.fiap.project.domain.dataprovider.PaymentDataProvider;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.usecase.impl.order.CalculateTotalOrderUseCaseImpl;
import tech.fiap.project.domain.usecase.payment.CreatePayment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;

public class CreatePaymentImpl implements CreatePayment {

	private final OrderDataProvider orderDataProvider;

	private final PaymentDataProvider paymentDataProvider;

	private final CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase;

	public CreatePaymentImpl(OrderDataProvider orderDataProvider, PaymentDataProvider paymentDataProvider,
			CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase) {
		this.orderDataProvider = orderDataProvider;
		this.paymentDataProvider = paymentDataProvider;
		this.calculateTotalOrderUseCase = calculateTotalOrderUseCase;
	}

	@Override
	public Payment execute(Order order) {
		LocalDateTime now = LocalDateTime.now();
		Currency currency = Currency.getInstance("BRL");
		Payment payment = new Payment(null, now, "PIX", calculateTotalOrderUseCase.execute(order.getItems()), currency,
				order, StatePayment.AWAITING);
		ArrayList<Payment> payments = new ArrayList<>();
		payments.add(payment);
		order.setPayments(payments);
		Order orderSaved = orderDataProvider.create(order);
		payment.setOrder(orderSaved);
		return paymentDataProvider.create(payment);
	}

}
