package tech.fiap.project.domain.usecase.impl.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.fiap.project.app.dto.StatePayment;
import tech.fiap.project.domain.dataprovider.OrderDataProvider;
import tech.fiap.project.domain.dataprovider.PaymentDataProvider;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.domain.usecase.impl.order.CalculateTotalOrderUseCaseImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreatePaymentImplTest {

	@Mock
	private OrderDataProvider orderDataProvider;

	@Mock
	private PaymentDataProvider paymentDataProvider;

	@Mock
	private CalculateTotalOrderUseCaseImpl calculateTotalOrderUseCase;

	@InjectMocks
	private CreatePaymentImpl createPaymentImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testExecute() {
		// Arrange
		Order order = new Order();
		order.setItems(new ArrayList<>());

		Payment payment = new Payment(null, LocalDateTime.now(), "PIX", BigDecimal.valueOf(100.0),
				Currency.getInstance("BRL"), order, StatePayment.AWAITING);
		Order orderSaved = new Order();
		orderSaved.setPayments(new ArrayList<>());
		orderSaved.getPayments().add(payment);

		when(calculateTotalOrderUseCase.execute(any())).thenReturn(BigDecimal.valueOf(100.0));
		when(orderDataProvider.create(any(Order.class))).thenReturn(orderSaved);
		when(paymentDataProvider.create(any(Payment.class))).thenReturn(payment);

		// Act
		Payment result = createPaymentImpl.execute(order);

		// Assert
		assertEquals(payment, result);
		verify(calculateTotalOrderUseCase, times(1)).execute(order.getItems());
		verify(orderDataProvider, times(1)).create(order);
		verify(paymentDataProvider, times(1)).create(payment);
	}

}