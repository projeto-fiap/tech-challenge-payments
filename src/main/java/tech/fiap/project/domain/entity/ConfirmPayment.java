package tech.fiap.project.domain.entity;

import lombok.Getter;
import lombok.Setter;
import tech.fiap.project.app.dto.StatePayment;

@Getter
@Setter
public class ConfirmPayment {

	private Order order;

	private StatePayment state;

}
