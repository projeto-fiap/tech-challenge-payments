package tech.fiap.project.app.adapter;

import org.junit.jupiter.api.Test;
import tech.fiap.project.domain.entity.Order;

import static org.junit.jupiter.api.Assertions.assertNull;

class OrderMapperTest {

	@Test
	void toDomain_shouldReturnNull_whenConfirmPaymentDTOIsNull() {
		Order order = OrderMapper.toDomain(null);
		assertNull(order);
	}

}