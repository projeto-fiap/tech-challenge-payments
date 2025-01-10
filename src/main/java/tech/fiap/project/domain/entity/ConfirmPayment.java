package tech.fiap.project.domain.entity;

import lombok.Data;
import tech.fiap.project.app.dto.StatePayment;

@Data
public class ConfirmPayment {

	private Order order;

	private StatePayment state;

}
