package tech.fiap.project.domain.usecase.impl.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.fiap.project.app.dto.StatePayment;
import tech.fiap.project.domain.dataprovider.PaymentDataProvider;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.entity.Item;
import tech.fiap.project.domain.usecase.impl.order.CalculateTotalOrderUseCaseImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CreatePaymentImplTest {

	@InjectMocks
	private CreatePaymentImpl createPayment;

	@Mock
	private PaymentDataProvider paymentDataProvider;

	@Mock
	private CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void execute_shouldCreatePaymentSuccessfully() {
		Order mockOrder = new Order();
		Item item1 = new Item();
		Item item2 = new Item();
		List<Item> items = Arrays.asList(item1, item2);
		mockOrder.setItems(items);

		BigDecimal totalOrderValue = BigDecimal.valueOf(35.0);
		Payment mockPayment = new Payment(null, LocalDateTime.now(), "PIX", totalOrderValue,
				Currency.getInstance("BRL"), mockOrder, StatePayment.AWAITING);

		when(calculateTotalOrderUseCase.execute(items)).thenReturn(totalOrderValue);
		when(paymentDataProvider.create(any(Payment.class))).thenReturn(mockPayment);

		Payment result = createPayment.execute(mockOrder);

		assertEquals("PIX", result.getPaymentMethod());
		assertEquals(StatePayment.AWAITING, result.getState());
		assertEquals(totalOrderValue, result.getAmount());
		assertEquals(mockOrder, result.getOrder());
		assertEquals(Currency.getInstance("BRL"), result.getCurrency());
		verify(calculateTotalOrderUseCase, times(1)).execute(items);
		verify(paymentDataProvider, times(1)).create(any(Payment.class));
	}

	@Test
	void execute_shouldHandleEmptyOrderItems() {
		Order mockOrder = new Order();
		mockOrder.setItems(List.of());
		BigDecimal totalOrderValue = BigDecimal.ZERO;
		Payment mockPayment = new Payment(null, LocalDateTime.now(), "PIX", totalOrderValue,
				Currency.getInstance("BRL"), mockOrder, StatePayment.AWAITING);
		when(paymentDataProvider.create(any(Payment.class))).thenReturn(mockPayment);
		when(calculateTotalOrderUseCase.execute(List.of())).thenReturn(totalOrderValue);
		Payment result = createPayment.execute(mockOrder);

		assertEquals(totalOrderValue, result.getAmount());
		assertEquals(StatePayment.AWAITING, result.getState());
		assertEquals(mockOrder, result.getOrder());
		verify(calculateTotalOrderUseCase, times(1)).execute(List.of());
		verify(paymentDataProvider, times(1)).create(any(Payment.class));
	}

	@Test
	void execute_shouldThrowException_whenPaymentDataProviderFails() {
		Order mockOrder = new Order();
		Item item = new Item();
		mockOrder.setItems(List.of(item));

		BigDecimal totalOrderValue = BigDecimal.valueOf(20.0);
		when(calculateTotalOrderUseCase.execute(mockOrder.getItems())).thenReturn(totalOrderValue);
		when(paymentDataProvider.create(any(Payment.class))).thenThrow(new RuntimeException("Database error"));

		try {
			createPayment.execute(mockOrder);
		}
		catch (RuntimeException e) {
			assertEquals("Database error", e.getMessage());
		}

		verify(calculateTotalOrderUseCase, times(1)).execute(mockOrder.getItems());
		verify(paymentDataProvider, times(1)).create(any(Payment.class));
	}

}
