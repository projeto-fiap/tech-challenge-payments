package tech.fiap.project.infra.dataprovider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.fiap.project.domain.entity.Item;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.infra.entity.OrderEntity;
import tech.fiap.project.infra.entity.PaymentEntity;
import tech.fiap.project.infra.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentDataProviderImplTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private EntityManager entityManager;

	@InjectMocks
	private PaymentDataProviderImpl paymentDataProvider;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void retrieveAll_ShouldReturnListOfPayments() {
		// Arrange
		PaymentEntity paymentEntity = new PaymentEntity();
		when(paymentRepository.findAll()).thenReturn(List.of(paymentEntity));

		// Act
		List<Payment> payments = paymentDataProvider.retrieveAll();

		// Assert
		assertNotNull(payments);
		assertEquals(1, payments.size());
		verify(paymentRepository, times(1)).findAll();
	}

	@Test
	void retrieveById_ShouldReturnPayment_WhenIdExists() {
		// Arrange
		Long paymentId = 1L;
		PaymentEntity paymentEntity = new PaymentEntity();
		Payment payment = new Payment(1L, null, null, null, null, null, null);
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(paymentEntity));

		// Act
		Optional<Payment> result = paymentDataProvider.retrieveById(paymentId);

		// Assert
		assertTrue(result.isPresent());
		verify(paymentRepository, times(1)).findById(paymentId);
	}

	@Test
	void retrieveById_ShouldReturnEmpty_WhenIdDoesNotExist() {
		// Arrange
		Long paymentId = 1L;
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

		// Act
		Optional<Payment> result = paymentDataProvider.retrieveById(paymentId);

		// Assert
		assertFalse(result.isPresent());
		verify(paymentRepository, times(1)).findById(paymentId);
	}

	@Test
	void create_ShouldSaveAndReturnPayment_WhenOrderExists() {
		// Arrange
		Payment payment = new Payment(1L, null, null, null, null, new Order(), null);
		Order order = new Order();
		ArrayList<Item> items = new ArrayList<>();
		Item item = new Item();
		item.setIngredients(new ArrayList<>());
		item.setId(1L);
		item.setQuantity(BigDecimal.valueOf(1L));
		item.setPrice(BigDecimal.valueOf(1.0));
		items.add(item);
		order.setItems(items);
		order.setId(1L);
		payment.setOrder(order);

		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(1L);

		PaymentEntity paymentEntity = new PaymentEntity();
		paymentEntity.setOrder(orderEntity);

		// Mock do entityManager para retornar o OrderEntity existente
		when(entityManager.find(OrderEntity.class, 1L)).thenReturn(orderEntity);

		// Act
		Payment result = paymentDataProvider.create(payment);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getOrder().getId()); // Verifica se o Order foi associado corretamente
		verify(entityManager, times(1)).find(OrderEntity.class, 1L); // Verifica se o entityManager.find foi chamado
		verify(paymentRepository, never()).save(any()); // Verifica que o save não foi chamado
	}

	@Test
	void create_ShouldThrowException_WhenOrderDoesNotExist() {
		// Arrange
		Payment payment = new Payment(1L, null, null, null, null, null, null);
		Order order = new Order();
		ArrayList<Item> objects = new ArrayList<>();
		Item e = new Item();
		e.setIngredients(new ArrayList<>());
		e.setId(1L);
		e.setQuantity(BigDecimal.valueOf(1L));
		e.setPrice(BigDecimal.valueOf(1.0));
		objects.add(e);
		order.setItems(objects);
		order.setId(1L);
		payment.setOrder(order);

		payment.setOrder(order);

		PaymentEntity paymentEntity = new PaymentEntity();
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(1L);
		paymentEntity.setOrder(orderEntity);

		when(entityManager.find(OrderEntity.class, 1L)).thenReturn(null);

		// Act & Assert
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
				() -> paymentDataProvider.create(payment));
		assertEquals("Order not found for id 1", exception.getMessage());
		verify(entityManager, times(1)).find(OrderEntity.class, 1L);
		verify(paymentRepository, never()).save(any());
	}

	@Test
	void create_ShouldReturnPaymentWithoutOrder_WhenNoOrderIsProvided() {
		// Arrange
		Payment payment = new Payment(1L, null, null, null, null, null, null);

		// Act
		Payment result = paymentDataProvider.create(payment);

		// Assert
		assertNotNull(result);
		assertNull(result.getOrder()); // Verifica se o Order é nulo
		verify(paymentRepository, never()).save(any()); // Verifica que o save não foi chamado
	}

}
