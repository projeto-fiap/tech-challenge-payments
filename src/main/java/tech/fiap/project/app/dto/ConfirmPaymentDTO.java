package tech.fiap.project.app.dto;

import lombok.Data;

@Data
public class ConfirmPaymentDTO {

	private OrderDTO order;

	private PaymentDTO paymentDTO;

	private StatePayment state;

}
