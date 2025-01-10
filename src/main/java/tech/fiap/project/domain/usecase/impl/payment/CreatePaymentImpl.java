package tech.fiap.project.domain.usecase.impl.payment;

import tech.fiap.project.app.dto.StatePayment;
import tech.fiap.project.domain.dataprovider.PaymentDataProvider;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.usecase.impl.order.CalculateTotalOrderUseCaseImpl;
import tech.fiap.project.domain.usecase.payment.CreatePayment;

import java.time.LocalDateTime;
import java.util.Currency;

public class CreatePaymentImpl implements CreatePayment {

    private final PaymentDataProvider paymentDataProvider;
    private final CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase;

    public CreatePaymentImpl(PaymentDataProvider paymentDataProvider, CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase) {
        this.paymentDataProvider = paymentDataProvider;
        this.calculateTotalOrderUseCase = calculateTotalOrderUseCase;
    }

    @Override
    public Payment execute(Order order) {
        LocalDateTime now = LocalDateTime.now();
        Currency currency = Currency.getInstance("BRL");
        Payment payment = new Payment(null,now, "PIX", calculateTotalOrderUseCase.execute(order.getItems()), currency, order, StatePayment.AWAITING);
        return paymentDataProvider.create(payment);
    }

}
