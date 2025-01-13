package tech.fiap.project.infra.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OrderEntityTest {

    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        orderEntity = new OrderEntity();
    }

    @Test
    void testAddItemsToOrder() {
        ItemEntity item1 = new ItemEntity();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setPrice(BigDecimal.valueOf(50.00));

        ItemEntity item2 = new ItemEntity();
        item2.setId(2L);
        item2.setName("Item 2");
        item2.setPrice(BigDecimal.valueOf(75.00));

        orderEntity.setItems(Arrays.asList(item1, item2));

        assertEquals(2, orderEntity.getItems().size(), "The items list should contain 2 items");
        assertEquals("Item 1", orderEntity.getItems().get(0).getName(), "The first item name should be 'Item 1'");
        assertEquals("Item 2", orderEntity.getItems().get(1).getName(), "The second item name should be 'Item 2'");
    }

    @Test
    void testSetItemsWithEmptyList() {
        orderEntity.setItems(new ArrayList<>());

        assertTrue(orderEntity.getItems().isEmpty(), "The items list should be empty after setting it to an empty list");
    }

    @Test
    void testSetItemsWithNull() {
        orderEntity.setItems(null);

        assertNull(orderEntity.getItems(), "The items list should be null after being set to null");
    }

    @Test
    void testAddItemToListAfterInitialization() {
        orderEntity.setItems(new ArrayList<>());

        ItemEntity item = new ItemEntity();
        item.setId(1L);
        item.setName("Item 1");
        item.setPrice(BigDecimal.valueOf(50.00));

        orderEntity.getItems().add(item);

        assertEquals(1, orderEntity.getItems().size(), "The items list should contain 1 item after adding it");
        assertEquals("Item 1", orderEntity.getItems().get(0).getName(), "The item added should be 'Item 1'");
    }
}
