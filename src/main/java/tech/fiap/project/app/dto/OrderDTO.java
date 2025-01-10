package tech.fiap.project.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class OrderDTO {

	private Long id;

	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private Duration awaitingTime;

	private BigDecimal totalPrice;

}