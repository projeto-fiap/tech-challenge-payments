package tech.fiap.project.domain.usecase.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.fiap.project.domain.entity.Item;
import tech.fiap.project.domain.usecase.impl.order.CalculateTotalOrderUseCaseImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateTotalOrderUseCaseTest {

	private CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase;

	@BeforeEach
	public void setUp() {
		calculateTotalOrderUseCase = new CalculateTotalOrderUseCaseImpl();
	}

	@Test
	public void testCalculateTotalOrder() {
		// Arrange
		Item item1 = new Item(); // Usando o construtor padrão
		item1.setName("Item1");
		item1.setPrice(new BigDecimal("10.00"));
		item1.setQuantity(BigDecimal.valueOf(2));

		Item item2 = new Item(); // Usando o construtor padrão
		item2.setName("Item2");
		item2.setPrice(new BigDecimal("20.00"));
		item2.setQuantity(BigDecimal.valueOf(1));

		List<Item> items = Arrays.asList(item1, item2);

		BigDecimal expectedTotal = new BigDecimal("40.00"); // Total esperado: (10 * 2) +
															// (20 * 1) = 40

		// Act
		BigDecimal actualTotal = calculateTotalOrderUseCase.execute(items);

		// Assert
		assertEquals(expectedTotal, actualTotal);
	}

	@Test
	public void testCalculateTotalOrder_WithIngredients() {
		// Arrange
		Item ingredient1 = new Item(); // Usando o construtor padrão
		ingredient1.setName("Ingredient1");
		ingredient1.setPrice(new BigDecimal("5.00"));
		ingredient1.setQuantity(BigDecimal.valueOf(1));

		Item ingredient2 = new Item(); // Usando o construtor padrão
		ingredient2.setName("Ingredient2");
		ingredient2.setPrice(new BigDecimal("3.00"));
		ingredient2.setQuantity(BigDecimal.valueOf(2));

		Item item1 = new Item(); // Usando o construtor padrão
		item1.setName("Item1");
		item1.setPrice(new BigDecimal("10.00"));
		item1.setQuantity(BigDecimal.valueOf(2));
		item1.setIngredients(Arrays.asList(ingredient1, ingredient2)); // Definindo
																		// ingredientes

		Item item2 = new Item(); // Usando o construtor padrão
		item2.setName("Item2");
		item2.setPrice(new BigDecimal("20.00"));
		item2.setQuantity(BigDecimal.valueOf(1));

		List<Item> items = Arrays.asList(item1, item2);

		BigDecimal expectedTotal = new BigDecimal("51.00");

		// Act
		BigDecimal actualTotal = calculateTotalOrderUseCase.execute(items);

		// Assert
		assertEquals(expectedTotal, actualTotal);
	}

}