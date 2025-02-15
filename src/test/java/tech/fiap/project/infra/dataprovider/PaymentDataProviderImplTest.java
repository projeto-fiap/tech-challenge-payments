package tech.fiap.project.infra.dataprovider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.infra.entity.OrderEntity;
import tech.fiap.project.infra.entity.PaymentEntity;
import tech.fiap.project.infra.mapper.PaymentRepositoryMapper;
import tech.fiap.project.infra.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentDataProviderImplTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private EntityManager entityManager;

	@InjectMocks
	private PaymentDataProviderImpl paymentDataProvider;

	private MockedStatic<PaymentRepositoryMapper> mockedMapper;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockedMapper = mockStatic(PaymentRepositoryMapper.class);
	}

	@AfterEach
	public void tearDown() {
		mockedMapper.close();
	}


	@Test
	public void testRetrieveAll() {
		PaymentEntity paymentEntity = new PaymentEntity();
		List<PaymentEntity> paymentEntities = List.of(paymentEntity);
		when(paymentRepository.findAll()).thenReturn(paymentEntities);

		List<Payment> payments = paymentDataProvider.retrieveAll();

		assertNotNull(payments);
		verify(paymentRepository, times(1)).findAll();
	}

	@Test
	public void testRetrieveById() {
		Long id = 1L;
		PaymentEntity paymentEntity = new PaymentEntity();
		Payment payment = new Payment();

		// Mock the repository to return the payment entity
		when(paymentRepository.findById(id)).thenReturn(Optional.of(paymentEntity));

		// Mock the static methods of PaymentRepositoryMapper
		mockedMapper.when(() -> PaymentRepositoryMapper.toDomainWithoutOrder(paymentEntity)).thenReturn(payment);

		Optional<Payment> retrievedPayment = paymentDataProvider.retrieveById(id);

		assertTrue(retrievedPayment.isPresent(), "Payment should be present");
		assertEquals(payment, retrievedPayment.get());
		verify(paymentRepository, times(1)).findById(id);
	}
	@Test
	public void testCreateWithExistingOrder() {
		Payment payment = new Payment();
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(1L);

		PaymentEntity paymentEntity = new PaymentEntity();
		paymentEntity.setOrder(orderEntity);

		// Mock the entity manager to return the order entity
		when(entityManager.find(OrderEntity.class, 1L)).thenReturn(orderEntity);

		// Mock the static methods of PaymentRepositoryMapper
		mockedMapper.when(() -> PaymentRepositoryMapper.toEntity(any(Payment.class))).thenReturn(paymentEntity);
		mockedMapper.when(() -> PaymentRepositoryMapper.toDomainWithOrder(any(PaymentEntity.class))).thenReturn(payment);

		Payment createdPayment = paymentDataProvider.create(payment);

		assertNotNull(createdPayment);
		verify(entityManager, times(1)).find(OrderEntity.class, 1L);
	}
}



