package tech.fiap.project.infra.dataprovider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.infra.entity.PaymentEntity;
import tech.fiap.project.infra.mapper.PaymentRepositoryMapper;
import tech.fiap.project.infra.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentDataProviderImplTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private PaymentRepositoryMapper paymentRepositoryMapper;

	private PaymentDataProviderImpl paymentDataProvider;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		paymentDataProvider = new PaymentDataProviderImpl(paymentRepository);
	}

	@Test
	void testRetrieveAll_shouldReturnPayments() {
		PaymentEntity payment1 = new PaymentEntity();
		PaymentEntity payment2 = new PaymentEntity();
		List<PaymentEntity> payments = List.of(payment1, payment2);

		when(paymentRepository.findAll()).thenReturn(payments);

		List<Payment> result = paymentDataProvider.retrieveAll();
		assertNotNull(result);
		assertEquals(2, result.size());
		verify(paymentRepository, times(1)).findAll();
	}

	@Test
	void testRetrieveById_shouldReturnPayment_whenFound() {
		Long paymentId = 1L;
		PaymentEntity payment = new PaymentEntity();
		payment.setId(paymentId);
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

		Optional<Payment> result = paymentDataProvider.retrieveById(paymentId);

		assertTrue(result.isPresent());
		assertEquals(paymentId, result.get().getId());
		verify(paymentRepository, times(1)).findById(paymentId);
	}

	@Test
	void testRetrieveById_shouldReturnEmpty_whenNotFound() {
		Long paymentId = 1L;

		when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

		Optional<Payment> result = paymentDataProvider.retrieveById(paymentId);

		assertFalse(result.isPresent());
		verify(paymentRepository, times(1)).findById(paymentId);
	}

	@Test
	void testCreate_shouldReturnCreatedPayment() {
		Payment payment = new Payment(1L, null, null, null, null, null, null);
		PaymentEntity savedPayment = new PaymentEntity();
		savedPayment.setId(1L);
		when(paymentRepository.save(any())).thenReturn(savedPayment);

		Payment result = paymentDataProvider.create(payment);

		assertNotNull(result);
		assertEquals(payment.getId(), result.getId());
		verify(paymentRepository, times(1)).save(any());
	}

	@Test
	void testCreate_shouldSetOrderToNull() {
		Payment payment = new Payment(1L, null, null, null, null, null, null);

		paymentDataProvider.create(payment);

		assertNull(payment.getOrder());
	}

}
