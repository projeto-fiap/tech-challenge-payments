package tech.fiap.project.infra.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class QItemEntityTest {

	private QItemEntity qItemEntity;

	@BeforeEach
	void setUp() {
		qItemEntity = new QItemEntity("itemEntity");
	}

	@Test
	void testConstructorWithString() {
		assertNotNull(qItemEntity);
		assertEquals("itemEntity", qItemEntity.getMetadata().getElement());
	}

	@Test
	void testConstructorWithPath() {
		Path<ItemEntity> path = new EntityPathBase<>(ItemEntity.class, "itemEntity");
		QItemEntity qItemEntityFromPath = new QItemEntity(path);
		assertNotNull(qItemEntityFromPath);
		assertEquals(path.getType(), qItemEntityFromPath.getType());
	}

	@Test
	void testConstructorWithPathMetadata() {
		PathMetadata metadata = forVariable("itemEntity");
		QItemEntity qItemEntityFromMetadata = new QItemEntity(metadata);
		assertNotNull(qItemEntityFromMetadata);
		assertEquals(ItemEntity.class, qItemEntityFromMetadata.getType());
	}

	@Test
	void testDescriptionField() {
		StringPath description = qItemEntity.description;
		assertNotNull(description);
		assertEquals("description", description.getMetadata().getName());
	}

	@Test
	void testIdField() {
		NumberPath<Long> id = qItemEntity.id;
		assertNotNull(id);
		assertEquals("id", id.getMetadata().getName());
		assertEquals(Long.class, id.getType());
	}

	@Test
	void testImageUrlField() {
		StringPath imageUrl = qItemEntity.imageUrl;
		assertNotNull(imageUrl);
		assertEquals("imageUrl", imageUrl.getMetadata().getName());
	}

	@Test
	void testIngredientsField() {
		ListPath<ItemEntity, QItemEntity> ingredients = qItemEntity.ingredients;
		assertNotNull(ingredients);
		assertEquals("ingredients", ingredients.getMetadata().getName());
		assertEquals(ItemEntity.class, ingredients.getElementType());
	}

	@Test
	void testNameField() {
		StringPath name = qItemEntity.name;
		assertNotNull(name);
		assertEquals("name", name.getMetadata().getName());
	}

	@Test
	void testPriceField() {
		NumberPath<BigDecimal> price = qItemEntity.price;
		assertNotNull(price);
		assertEquals("price", price.getMetadata().getName());
		assertEquals(BigDecimal.class, price.getType());
	}

	@Test
	void testQuantityField() {
		NumberPath<BigDecimal> quantity = qItemEntity.quantity;
		assertNotNull(quantity);
		assertEquals("quantity", quantity.getMetadata().getName());
		assertEquals(BigDecimal.class, quantity.getType());
	}

	@Test
	void testUnitField() {
		StringPath unit = qItemEntity.unit;
		assertNotNull(unit);
		assertEquals("unit", unit.getMetadata().getName());
	}

}