package tech.fiap.project.app.adapter;

import org.junit.jupiter.api.Test;
import tech.fiap.project.app.dto.ConfirmPaymentDTO;
import tech.fiap.project.app.dto.OrderDTO;
import tech.fiap.project.app.dto.StatePayment;
import tech.fiap.project.domain.entity.ConfirmPayment;

import static org.junit.jupiter.api.Assertions.*;

class ConfirmPaymentMapperTest {

	@Test
	void toDomain_shouldReturnNull_whenConfirmPaymentDTOIsNull() {
		ConfirmPayment confirmPayment = ConfirmPaymentMapper.toDomain(null);
		assertNull(confirmPayment);
	}

	@Test
	void toDomain_shouldMapFieldsCorrectly_whenConfirmPaymentDTOIsNotNull() {
		ConfirmPaymentDTO confirmPaymentDTO = new ConfirmPaymentDTO();
		confirmPaymentDTO.setState(StatePayment.ACCEPTED);
		confirmPaymentDTO.setOrder(new OrderDTO());

		ConfirmPayment confirmPayment = ConfirmPaymentMapper.toDomain(confirmPaymentDTO);

		assertEquals(confirmPaymentDTO.getState(), confirmPayment.getState());
		assertNotNull(confirmPayment.getOrder());
	}

}