package tech.fiap.project.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmPaymentDTO {

	private OrderDTO order;

	private PaymentDTO paymentDTO;

	private StatePayment state;

}
