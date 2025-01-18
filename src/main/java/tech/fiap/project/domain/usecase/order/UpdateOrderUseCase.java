package tech.fiap.project.domain.usecase.order;

import tech.fiap.project.domain.entity.Order;

import java.util.Optional;

public interface UpdateOrderUseCase {

	Optional<Order> updateOrder(Long orderId);

}
