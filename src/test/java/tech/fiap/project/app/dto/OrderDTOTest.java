package tech.fiap.project.app.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderDTOTest {

	private OrderDTO order;

	@BeforeEach
	void setUp() {
		order = new OrderDTO();
	}

	@Test
	void testNoArgsConstructor() {
		assertNotNull(order);
		assertNull(order.getId());
		assertNull(order.getCreatedDate());
		assertNull(order.getUpdatedDate());
		assertNull(order.getAwaitingTime());
		assertNull(order.getTotalPrice());
	}

	@Test
	void testAllArgsConstructor() {
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.ofMinutes(30);
		BigDecimal price = BigDecimal.valueOf(99.99);
		OrderDTO newOrder = new OrderDTO(1L, now, now, duration, price);

		assertNotNull(newOrder);
		assertEquals(1L, newOrder.getId());
		assertEquals(now, newOrder.getCreatedDate());
		assertEquals(now, newOrder.getUpdatedDate());
		assertEquals(duration, newOrder.getAwaitingTime());
		assertEquals(price, newOrder.getTotalPrice());
	}

	@Test
	void testSettersAndGetters() {
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.ofMinutes(45);
		BigDecimal price = BigDecimal.valueOf(150.50);

		order.setId(2L);
		order.setCreatedDate(now);
		order.setUpdatedDate(now);
		order.setAwaitingTime(duration);
		order.setTotalPrice(price);

		assertEquals(2L, order.getId());
		assertEquals(now, order.getCreatedDate());
		assertEquals(now, order.getUpdatedDate());
		assertEquals(duration, order.getAwaitingTime());
		assertEquals(price, order.getTotalPrice());
	}

}
