package tech.fiap.project.app.adapter;

import tech.fiap.project.app.dto.ConfirmPaymentDTO;
import tech.fiap.project.domain.entity.ConfirmPayment;

public class ConfirmPaymentMapper {

	private ConfirmPaymentMapper() {

	}

	public static ConfirmPayment toDomain(ConfirmPaymentDTO confirmPaymentDTO) {
		if (confirmPaymentDTO ==null){
			return null;
		}
		ConfirmPayment confirmPayment = new ConfirmPayment();
		confirmPayment.setState(confirmPaymentDTO.getState());
		confirmPayment.setOrder(OrderMapper.toDomain(confirmPaymentDTO.getOrder()));
		return confirmPayment;
	}
}
