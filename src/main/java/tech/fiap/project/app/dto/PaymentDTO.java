package tech.fiap.project.app.dto;

import lombok.Data;
import tech.fiap.project.domain.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Data
public class PaymentDTO {

	private Long id;

	private LocalDateTime paymentDate;

	private String paymentMethod;

	private BigDecimal amount;

	private Currency currency;

	private StatePayment state;

	private Order order;

}
