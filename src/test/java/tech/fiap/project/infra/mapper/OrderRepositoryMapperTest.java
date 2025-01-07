package tech.fiap.project.infra.mapper;

import org.junit.jupiter.api.Test;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.infra.entity.OrderEntity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryMapperTest {


	@Test
	void toEntity_shouldReturnNullWhenOrderIsNull() {
		OrderEntity orderEntity = OrderRepositoryMapper.toEntity(null);
		assertNull(orderEntity);
	}

	@Test
	void toEntity_shouldMapOrderToOrderEntity() {
		Order order = new Order(1L, LocalDateTime.now(), LocalDateTime.now(), List.of(), List.of(), Duration.ofDays(1), BigDecimal.valueOf(100.0));
		OrderEntity orderEntity = OrderRepositoryMapper.toEntity(order);

		assertNotNull(orderEntity);
		assertEquals(order.getId(), orderEntity.getId());
		assertEquals(order.getCreatedDate(), orderEntity.getCreatedDate());
		assertEquals(order.getUpdatedDate(), orderEntity.getUpdatedDate());
		assertEquals(order.getAwaitingTime(), orderEntity.getAwaitingTime());
		assertEquals(order.getTotalPrice(), orderEntity.getTotalPrice());
	}

	@Test
	void toDomain_shouldReturnNullWhenOrderEntityIsNull() {
		Order order = OrderRepositoryMapper.toDomain(null);
		assertNull(order);
	}

	@Test
	void toDomain_shouldMapOrderEntityToOrder() {
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(1L);
		orderEntity.setCreatedDate(LocalDateTime.now());
		orderEntity.setUpdatedDate(LocalDateTime.now());
		orderEntity.setAwaitingTime(Duration.ofDays(1));
		orderEntity.setTotalPrice(BigDecimal.valueOf(100.0));

		Order order = OrderRepositoryMapper.toDomain(orderEntity);

		assertNotNull(order);
		assertEquals(orderEntity.getId(), order.getId());
		assertEquals(orderEntity.getCreatedDate(), order.getCreatedDate());
		assertEquals(orderEntity.getUpdatedDate(), order.getUpdatedDate());
		assertEquals(orderEntity.getAwaitingTime(), order.getAwaitingTime());
		assertEquals(orderEntity.getTotalPrice(), order.getTotalPrice());
	}

}