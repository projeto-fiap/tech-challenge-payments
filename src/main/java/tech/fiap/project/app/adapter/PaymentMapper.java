package tech.fiap.project.app.adapter;

import tech.fiap.project.app.dto.PaymentDTO;
import tech.fiap.project.domain.entity.Payment;

public class PaymentMapper {

	private PaymentMapper() {

	}

	public static PaymentDTO toDomain(tech.fiap.project.domain.entity.Payment payment) {
		if (payment == null) {
			return null;
		}
		else {
			PaymentDTO paymentDTO = new PaymentDTO();
			paymentDTO.setId(payment.getId());
			paymentDTO.setOrder(payment.getOrder());
			paymentDTO.setAmount(payment.getAmount());
			paymentDTO.setCurrency(payment.getCurrency());
			paymentDTO.setPaymentDate(payment.getPaymentDate());
			paymentDTO.setPaymentMethod(payment.getPaymentMethod());
			paymentDTO.setState(payment.getState());
			if (payment.getOrder() != null) {
				paymentDTO.setOrder(payment.getOrder());
			}
			return paymentDTO;
		}
	}

	public static PaymentDTO toDomainWithoutOrder(tech.fiap.project.domain.entity.Payment payment) {
		if (payment == null) {
			return null;
		}
		else {
			PaymentDTO paymentDTO = new PaymentDTO();
			paymentDTO.setAmount(payment.getAmount());
			paymentDTO.setId(payment.getId());
			paymentDTO.setCurrency(payment.getCurrency());
			paymentDTO.setPaymentDate(payment.getPaymentDate());
			paymentDTO.setPaymentMethod(payment.getPaymentMethod());
			paymentDTO.setState(payment.getState());
			paymentDTO.setOrder(payment.getOrder());
			return paymentDTO;
		}
	}

	public static Payment toDTO(PaymentDTO payment) {
		if (payment == null) {
			return null;
		}
		else {
			return new Payment(payment.getId(),payment.getPaymentDate(), payment.getPaymentMethod(), payment.getAmount(),
					payment.getCurrency(), payment.getOrder(), payment.getState());
		}
	}

}
