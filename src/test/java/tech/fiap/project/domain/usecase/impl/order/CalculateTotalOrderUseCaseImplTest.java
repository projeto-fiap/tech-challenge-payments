package tech.fiap.project.domain.usecase.impl.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.fiap.project.domain.entity.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateTotalOrderUseCaseImplTest {

	private CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase;

	@BeforeEach
	void setUp() {
		calculateTotalOrderUseCase = new CalculateTotalOrderUseCaseImpl();
	}

	@Test
	void execute_shouldCalculateTotalOrder() {
		Item item1 = new Item(1L, "name", BigDecimal.ONE, BigDecimal.ONE, "unit", null, "descrição", null);
		item1.setPrice(BigDecimal.valueOf(10));
		item1.setQuantity(BigDecimal.valueOf(2));

		Item item2 = new Item(3L, "name3", BigDecimal.ONE, BigDecimal.ONE, "unit", null, "descrição", null);
		item2.setPrice(BigDecimal.valueOf(5));
		item2.setQuantity(BigDecimal.valueOf(3));

		List<Item> items = List.of(item1, item2);

		BigDecimal total = calculateTotalOrderUseCase.execute(items);

		assertEquals(BigDecimal.valueOf(35), total);
	}

	@Test
	void execute_shouldCalculateTotalOrderWithIngredients() {
		Item ingredient1 = new Item(1L, "name", BigDecimal.ONE, BigDecimal.ONE, "unit", null, "descrição", null);
		ingredient1.setPrice(BigDecimal.valueOf(2));
		ingredient1.setQuantity(BigDecimal.valueOf(1));

		Item ingredient2 = new Item(2L, "name2", BigDecimal.ONE, BigDecimal.ONE, "unit", null, "descrição", null);
		ingredient2.setPrice(BigDecimal.valueOf(3));
		ingredient2.setQuantity(BigDecimal.valueOf(1));

		Item item = new Item(3L, "name3", BigDecimal.ONE, BigDecimal.ONE, "unit", null, "descrição", null);
		item.setPrice(BigDecimal.valueOf(10));
		item.setQuantity(BigDecimal.valueOf(1));
		item.setIngredients(List.of(ingredient1, ingredient2));

		List<Item> items = List.of(item);

		BigDecimal total = calculateTotalOrderUseCase.execute(items);

		assertEquals(BigDecimal.valueOf(15), total);
	}

	@Test
	void buildListIngredients_shouldHandleNonNullAndNonEmptyIngredients() {
		// Arrange: Criação de itens com ingredientes
		Item subIngredient1 = new Item(1L, "Tomato", BigDecimal.valueOf(2.50), BigDecimal.ONE, "unit",
				new ArrayList<>(), "", "");
		Item subIngredient2 = new Item(2L, "Cheese", BigDecimal.valueOf(5.00), BigDecimal.ONE, "unit",
				new ArrayList<>(), "", "");

		List<Item> ingredientList = new ArrayList<>();
		ingredientList.add(subIngredient1);
		ingredientList.add(subIngredient2);

		Item mainIngredient = new Item(3L, "Pizza Base", BigDecimal.valueOf(15.00), BigDecimal.ONE, "UNIT",
				ingredientList, "", "");

		List<Item> mainIngredientsList = new ArrayList<>();
		mainIngredientsList.add(mainIngredient);

		// Act: Chama a função buildListIngredients diretamente
		List<Item> result = calculateTotalOrderUseCase.buildListIngredients(mainIngredientsList);

		// Assert: Verifica se os ingredientes foram processados corretamente
		assertEquals(2, result.size()); // Deve conter os dois subingredientes (Tomato e
										// Cheese)
		assertEquals("Tomato", result.get(0).getName());
		assertEquals("Cheese", result.get(1).getName());
	}

}