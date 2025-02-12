package tech.fiap.project.infra.dataprovider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.fiap.project.domain.dataprovider.PaymentDataProvider;
import tech.fiap.project.domain.entity.Payment;
import tech.fiap.project.infra.entity.OrderEntity;
import tech.fiap.project.infra.entity.PaymentEntity;
import tech.fiap.project.infra.mapper.PaymentRepositoryMapper;
import tech.fiap.project.infra.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentDataProviderImpl implements PaymentDataProvider {

	private PaymentRepository paymentRepository;

	private EntityManager entityManager;

	@Override
	public List<Payment> retrieveAll() {
		return paymentRepository.findAll().stream().map(PaymentRepositoryMapper::toDomainWithOrder).toList();
	}

	@Override
	public Optional<Payment> retrieveById(Long id) {
		return paymentRepository.findById(id).map(PaymentRepositoryMapper::toDomainWithoutOrder);
	}

	@Override
	public Payment create(Payment payment) {
		PaymentEntity paymentEntity = PaymentRepositoryMapper.toEntity(payment);

		if (paymentEntity.getOrder() != null && paymentEntity.getOrder().getId() != null) {
			OrderEntity existingOrder = entityManager.find(OrderEntity.class, paymentEntity.getOrder().getId());
			if (existingOrder != null) {
				paymentEntity.setOrder(existingOrder);
			}
			else {
				throw new EntityNotFoundException("Order not found for id " + paymentEntity.getOrder().getId());
			}
		}

		return PaymentRepositoryMapper.toDomainWithOrder(paymentEntity);
	}

}
