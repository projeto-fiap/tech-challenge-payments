package tech.fiap.project.infra.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class QOrderEntityTest {

	private QOrderEntity qOrderEntity;

	@BeforeEach
	void setUp() {
		qOrderEntity = new QOrderEntity("orderEntity");
	}

	@Test
	void testConstructorWithString() {
		assertNotNull(qOrderEntity);
		assertEquals("orderEntity", qOrderEntity.getMetadata().getElement());
	}

	@Test
	void testConstructorWithPath() {
		Path<OrderEntity> path = new EntityPathBase<>(OrderEntity.class, "orderEntity");
		QOrderEntity qOrderEntityFromPath = new QOrderEntity(path);
		assertNotNull(qOrderEntityFromPath);
		assertEquals(path.getType(), qOrderEntityFromPath.getType());
	}

	@Test
	void testConstructorWithPathMetadata() {
		PathMetadata metadata = forVariable("orderEntity");
		QOrderEntity qOrderEntityFromMetadata = new QOrderEntity(metadata);
		assertNotNull(qOrderEntityFromMetadata);
		assertEquals(OrderEntity.class, qOrderEntityFromMetadata.getType());
	}

	@Test
	void testAwaitingTimeField() {
		ComparablePath<Duration> awaitingTime = qOrderEntity.awaitingTime;
		assertNotNull(awaitingTime);
		assertEquals("awaitingTime", awaitingTime.getMetadata().getName());
		assertEquals(Duration.class, awaitingTime.getType());
	}

	@Test
	void testCreatedDateField() {
		DateTimePath<LocalDateTime> createdDate = qOrderEntity.createdDate;
		assertNotNull(createdDate);
		assertEquals("createdDate", createdDate.getMetadata().getName());
		assertEquals(LocalDateTime.class, createdDate.getType());
	}

	@Test
	void testIdField() {
		NumberPath<Long> id = qOrderEntity.id;
		assertNotNull(id);
		assertEquals("id", id.getMetadata().getName());
		assertEquals(Long.class, id.getType());
	}

	@Test
	void testItemsField() {
		ListPath<ItemEntity, QItemEntity> items = qOrderEntity.items;
		assertNotNull(items);
		assertEquals("items", items.getMetadata().getName());
		assertEquals(ItemEntity.class, items.getElementType());
	}

	@Test
	void testPaymentsField() {
		ListPath<PaymentEntity, QPaymentEntity> payments = qOrderEntity.payments;
		assertNotNull(payments);
		assertEquals("payments", payments.getMetadata().getName());
		assertEquals(PaymentEntity.class, payments.getElementType());
	}

	@Test
	void testTotalPriceField() {
		NumberPath<BigDecimal> totalPrice = qOrderEntity.totalPrice;
		assertNotNull(totalPrice);
		assertEquals("totalPrice", totalPrice.getMetadata().getName());
		assertEquals(BigDecimal.class, totalPrice.getType());
	}

	@Test
	void testUpdatedDateField() {
		DateTimePath<LocalDateTime> updatedDate = qOrderEntity.updatedDate;
		assertNotNull(updatedDate);
		assertEquals("updatedDate", updatedDate.getMetadata().getName());
		assertEquals(LocalDateTime.class, updatedDate.getType());
	}

}