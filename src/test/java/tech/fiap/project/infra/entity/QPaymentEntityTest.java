package tech.fiap.project.infra.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;
import static org.junit.jupiter.api.Assertions.*;

class QPaymentEntityTest {

	private QPaymentEntity qPaymentEntity;

	@BeforeEach
	void setUp() {
		qPaymentEntity = new QPaymentEntity("paymentEntity");
	}

	@Test
	void testConstructorWithString() {
		assertNotNull(qPaymentEntity);
		assertEquals("paymentEntity", qPaymentEntity.getMetadata().getElement());
	}

	@Test
	void testConstructorWithPath() {
		Path<PaymentEntity> path = new EntityPathBase<>(PaymentEntity.class, "paymentEntity");
		QPaymentEntity qPaymentEntityFromPath = new QPaymentEntity(path);
		assertNotNull(qPaymentEntityFromPath);
		assertEquals(path.getType(), qPaymentEntityFromPath.getType());
	}

	@Test
	void testConstructorWithPathMetadata() {
		PathMetadata metadata = forVariable("paymentEntity");
		QPaymentEntity qPaymentEntityFromMetadata = new QPaymentEntity(metadata);
		assertNotNull(qPaymentEntityFromMetadata);
		assertEquals(PaymentEntity.class, qPaymentEntityFromMetadata.getType());
	}

	@Test
	void testConstructorWithPathMetadataAndInits() {
		PathMetadata metadata = forVariable("paymentEntity");
		PathInits inits = PathInits.DIRECT2;
		QPaymentEntity qPaymentEntityFromMetadataAndInits = new QPaymentEntity(metadata, inits);
		assertNotNull(qPaymentEntityFromMetadataAndInits);
		assertEquals(PaymentEntity.class, qPaymentEntityFromMetadataAndInits.getType());
	}

	@Test
	void testConstructorWithClassPathMetadataAndInits() {
		PathMetadata metadata = forVariable("paymentEntity");
		PathInits inits = PathInits.DIRECT2;
		QPaymentEntity qPaymentEntityFromClassMetadataAndInits = new QPaymentEntity(PaymentEntity.class, metadata,
				inits);
		assertNotNull(qPaymentEntityFromClassMetadataAndInits);
		assertEquals(PaymentEntity.class, qPaymentEntityFromClassMetadataAndInits.getType());
	}

	@Test
	void testAmountField() {
		NumberPath<BigDecimal> amount = qPaymentEntity.amount;
		assertNotNull(amount);
		assertEquals("amount", amount.getMetadata().getName());
		assertEquals(BigDecimal.class, amount.getType());
	}

	@Test
	void testCurrencyField() {
		SimplePath<Currency> currency = qPaymentEntity.currency;
		assertNotNull(currency);
		assertEquals("currency", currency.getMetadata().getName());
		assertEquals(Currency.class, currency.getType());
	}

	@Test
	void testIdField() {
		NumberPath<Long> id = qPaymentEntity.id;
		assertNotNull(id);
		assertEquals("id", id.getMetadata().getName());
		assertEquals(Long.class, id.getType());
	}

	@Test
	void testOrderField() {
		QOrderEntity order = qPaymentEntity.order;
		assertNotNull(order);
		assertEquals("order", order.getMetadata().getElement());
	}

	@Test
	void testPaymentDateField() {
		DateTimePath<LocalDateTime> paymentDate = qPaymentEntity.paymentDate;
		assertNotNull(paymentDate);
		assertEquals("paymentDate", paymentDate.getMetadata().getName());
		assertEquals(LocalDateTime.class, paymentDate.getType());
	}

	@Test
	void testPaymentMethodField() {
		StringPath paymentMethod = qPaymentEntity.paymentMethod;
		assertNotNull(paymentMethod);
		assertEquals("paymentMethod", paymentMethod.getMetadata().getName());
	}

	@Test
	void testStateField() {
		EnumPath<tech.fiap.project.app.dto.StatePayment> state = qPaymentEntity.state;
		assertNotNull(state);
		assertEquals("state", state.getMetadata().getName());
		assertEquals(tech.fiap.project.app.dto.StatePayment.class, state.getType());
	}

}