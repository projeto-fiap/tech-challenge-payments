package tech.fiap.project.domain.usecase.payment;

import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;

public interface CreatePayment {

    Payment execute(Order order);

}
