package tech.fiap.project.infra.dataprovider;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.fiap.project.domain.dataprovider.OrderDataProvider;
import tech.fiap.project.domain.entity.Order;
import tech.fiap.project.infra.entity.ItemEntity;
import tech.fiap.project.infra.entity.OrderEntity;
import tech.fiap.project.infra.mapper.ItemRepositoryMapper;
import tech.fiap.project.infra.mapper.OrderRepositoryMapper;
import tech.fiap.project.infra.repository.ItemRepository;
import tech.fiap.project.infra.repository.OrderRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderDataProviderImpl implements OrderDataProvider {

	private OrderRepository orderRepository;

	private ItemRepository itemRepository;

	@Override
	public Order create(Order order) {

		OrderEntity entity = OrderRepositoryMapper.toEntityPayment(order);
		List<ItemEntity> items = itemRepository
				.saveAll(order.getItems().stream().map(ItemRepositoryMapper::toEntity).toList());
		entity.setItems(items);
		OrderEntity orderSaved = orderRepository.save(entity);
		return OrderRepositoryMapper.toDomainWithPayment(orderSaved);
	}

	@Override
	public Order retrieveByPaymentId(Long paymentId) {
		return orderRepository.findByPaymentId(paymentId).map(OrderRepositoryMapper::toDomainWithPayment).orElse(null);
	}

}
