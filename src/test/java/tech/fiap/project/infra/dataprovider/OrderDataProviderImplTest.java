package tech.fiap.project.infra.dataprovider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.infra.entity.ItemEntity;
import tech.fiap.project.infra.entity.OrderEntity;
import tech.fiap.project.infra.mapper.ItemRepositoryMapper;
import tech.fiap.project.infra.mapper.OrderRepositoryMapper;
import tech.fiap.project.infra.repository.ItemRepository;
import tech.fiap.project.infra.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderDataProviderImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private OrderDataProviderImpl orderDataProvider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    @Test
    public void testCreateOrder() {
        // Arrange
        Order order = new Order();
        order.setItems(Collections.emptyList()); // Lista vazia de itens

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setCreatedDate(LocalDateTime.now());
        orderEntity.setUpdatedDate(LocalDateTime.now());
        orderEntity.setItems(Collections.emptyList());
        orderEntity.setAwaitingTime(Duration.ofMinutes(30));
        orderEntity.setTotalPrice(BigDecimal.valueOf(100.00));

        // Simula o comportamento do mapper (método estático)
        try (var mockedMapper = mockStatic(OrderRepositoryMapper.class)) {
            mockedMapper.when(() -> OrderRepositoryMapper.toEntityPayment(order)).thenReturn(orderEntity);

            // Simula o comportamento do itemRepository
            when(itemRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

            // Simula o comportamento do orderRepository
            when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

            // Simula o comportamento do mapper para retornar o pedido salvo
            mockedMapper.when(() -> OrderRepositoryMapper.toDomainWithPayment(orderEntity)).thenReturn(order);

            // Act
            Order createdOrder = orderDataProvider.create(order);

            // Assert
            assertNotNull(createdOrder); // Verifica se o pedido foi criado
            mockedMapper.verify(() -> OrderRepositoryMapper.toEntityPayment(order), times(1));
            verify(itemRepository, times(1)).saveAll(anyList());
            verify(orderRepository, times(1)).save(orderEntity);
            mockedMapper.verify(() -> OrderRepositoryMapper.toDomainWithPayment(orderEntity), times(1));
        }
    }

    @Test
    public void testRetrieveByPaymentId_OrderExists() {
        // Arrange
        Long paymentId = 123L;
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setCreatedDate(LocalDateTime.now());
        orderEntity.setUpdatedDate(LocalDateTime.now());
        orderEntity.setItems(Collections.emptyList());
        orderEntity.setAwaitingTime(Duration.ofMinutes(30));
        orderEntity.setTotalPrice(BigDecimal.valueOf(100.00));

        Order order = new Order();

        // Simula o comportamento do orderRepository
        when(orderRepository.findByPaymentId(paymentId)).thenReturn(Optional.of(orderEntity));

        // Simula o comportamento do mapper (método estático)
        try (var mockedMapper = mockStatic(OrderRepositoryMapper.class)) {
            mockedMapper.when(() -> OrderRepositoryMapper.toDomainWithPayment(orderEntity)).thenReturn(order);

            // Act
            Order retrievedOrder = orderDataProvider.retrieveByPaymentId(paymentId);

            // Assert
            assertNotNull(retrievedOrder); // Verifica se o pedido foi encontrado
            verify(orderRepository, times(1)).findByPaymentId(paymentId);
            mockedMapper.verify(() -> OrderRepositoryMapper.toDomainWithPayment(orderEntity), times(1));
        }
    }

    @Test
    public void testRetrieveByPaymentId_OrderDoesNotExist() {
        // Arrange
        Long paymentId = 123L;

        // Simula o comportamento do orderRepository (pedido não encontrado)
        when(orderRepository.findByPaymentId(paymentId)).thenReturn(Optional.empty());

        // Act
        Order retrievedOrder = orderDataProvider.retrieveByPaymentId(paymentId);

        // Assert
        assertNull(retrievedOrder); // Verifica que o pedido não foi encontrado
        verify(orderRepository, times(1)).findByPaymentId(paymentId);
    }
}