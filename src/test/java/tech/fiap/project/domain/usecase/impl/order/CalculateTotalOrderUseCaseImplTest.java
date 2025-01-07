package tech.fiap.project.domain.usecase.impl.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.fiap.project.domain.entity.Item;

import java.math.BigDecimal;
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
        Item item1 = new Item(1L,"name",BigDecimal.ONE,BigDecimal.ONE,"unit",null,"descrição",null);
        item1.setPrice(BigDecimal.valueOf(10));
        item1.setQuantity(BigDecimal.valueOf(2));

        Item item2 = new Item(3L,"name3",BigDecimal.ONE,BigDecimal.ONE,"unit",null,"descrição",null);
        item2.setPrice(BigDecimal.valueOf(5));
        item2.setQuantity(BigDecimal.valueOf(3));

        List<Item> items = List.of(item1, item2);

        BigDecimal total = calculateTotalOrderUseCase.execute(items);

        assertEquals(BigDecimal.valueOf(35), total);
    }

    @Test
    void execute_shouldCalculateTotalOrderWithIngredients() {
        Item ingredient1 = new Item(1L,"name",BigDecimal.ONE,BigDecimal.ONE,"unit",null,"descrição",null);
        ingredient1.setPrice(BigDecimal.valueOf(2));
        ingredient1.setQuantity(BigDecimal.valueOf(1));

        Item ingredient2 = new Item(2L,"name2",BigDecimal.ONE,BigDecimal.ONE,"unit",null,"descrição",null);
        ingredient2.setPrice(BigDecimal.valueOf(3));
        ingredient2.setQuantity(BigDecimal.valueOf(1));

        Item item = new Item(3L,"name3",BigDecimal.ONE,BigDecimal.ONE,"unit",null,"descrição",null);
        item.setPrice(BigDecimal.valueOf(10));
        item.setQuantity(BigDecimal.valueOf(1));
        item.setIngredients(List.of(ingredient1, ingredient2));

        List<Item> items = List.of(item);

        BigDecimal total = calculateTotalOrderUseCase.execute(items);

        assertEquals(BigDecimal.valueOf(15), total);
    }
}