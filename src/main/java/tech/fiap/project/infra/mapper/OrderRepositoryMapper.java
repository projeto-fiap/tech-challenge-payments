package tech.fiap.project.infra.mapper;

import tech.fiap.project.domain.entity.Item;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.infra.entity.OrderEntity;

import java.util.List;

public class OrderRepositoryMapper {

	private OrderRepositoryMapper() {
	}

	public static OrderEntity toEntityPayment(Order order) {
		if (order == null) {
			return null;
		}
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(order.getId());

		if (order.getPayments() != null) {
			orderEntity.setPayments(
					order.getPayments().stream().map(PaymentRepositoryMapper::toEntityWithoutOrder).toList());
		}
		orderEntity.setItems(order.getItems().stream().map(ItemRepositoryMapper::toEntity).toList());
		orderEntity.setCreatedDate(order.getCreatedDate());
		orderEntity.setUpdatedDate(order.getUpdatedDate());
		orderEntity.setAwaitingTime(order.getAwaitingTime());
		orderEntity.setTotalPrice(order.getTotalPrice());
		return orderEntity;
	}

	public static Order toDomainWithPayment(OrderEntity orderEntity) {
		if (orderEntity == null) {
			return null;
		}
		List<Payment> payments = null;
		if (orderEntity.getPayments() != null) {
			payments = orderEntity.getPayments().stream().map(PaymentRepositoryMapper::toDomainWithOrder).toList();
		}

		List<Item> items = null;
		if (orderEntity.getPayments() != null) {
			items = orderEntity.getItems().stream().map(ItemRepositoryMapper::toDomain).toList();
		}

		return new Order(orderEntity.getId(), orderEntity.getCreatedDate(), orderEntity.getUpdatedDate(), items,
				payments, orderEntity.getAwaitingTime(), orderEntity.getTotalPrice());
	}

	public static Order toDomainWithoutPayment(OrderEntity orderEntity) {
		if (orderEntity == null) {
			return null;
		}
		List<Payment> payments = null;

		List<Item> items = null;
		if (orderEntity.getItems() != null) {
			items = orderEntity.getItems().stream().map(ItemRepositoryMapper::toDomain).toList();
		}

		return new Order(orderEntity.getId(), orderEntity.getCreatedDate(), orderEntity.getUpdatedDate(), items,
				payments, orderEntity.getAwaitingTime(), orderEntity.getTotalPrice());
	}

}
