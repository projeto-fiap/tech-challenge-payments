package tech.fiap.project.app.adapter;

import tech.fiap.project.app.dto.OrderDTO;
import tech.fiap.project.domain.entity.Order;

public class OrderMapper {

	private OrderMapper() {

	}

	public static Order toDomain(OrderDTO orderDTO) {
		if (orderDTO == null) {
			return null;
		}
		else {
			return new Order(orderDTO.getId(), orderDTO.getCreatedDate(), orderDTO.getUpdatedDate(), null, null, null,
					orderDTO.getTotalPrice());
		}
	}

}
