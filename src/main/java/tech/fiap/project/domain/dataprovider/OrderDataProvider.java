package tech.fiap.project.domain.dataprovider;

import tech.fiap.project.domain.entity.Order;

public interface OrderDataProvider {

	Order create(Order order);

	Order retrieveByPaymentId(Long paymentId);

}
