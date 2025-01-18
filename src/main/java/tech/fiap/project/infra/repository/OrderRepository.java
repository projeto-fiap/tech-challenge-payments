package tech.fiap.project.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.fiap.project.infra.entity.OrderEntity;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

	@Query(value = "SELECT o.* FROM \"order\" o " + "JOIN order_payments op ON o.id = op.order_id "
			+ "WHERE op.payment_id = :paymentId", nativeQuery = true)
	Optional<OrderEntity> findByPaymentId(@Param("paymentId") Long paymentId);

}
