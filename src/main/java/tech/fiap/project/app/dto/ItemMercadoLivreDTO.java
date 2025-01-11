package tech.fiap.project.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ItemMercadoLivreDTO {

	private String skuNumber;

	private String category;

	private String title;

	private String description;

	@JsonProperty("unit_price")
	private BigDecimal unitPrice;

	private Integer quantity;

	@JsonProperty("unit_measure")
	private String unitMeasure;

	@JsonProperty("total_amount")
	private BigDecimal totalAmount;

}