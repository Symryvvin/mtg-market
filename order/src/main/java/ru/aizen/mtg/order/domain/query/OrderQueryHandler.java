package ru.aizen.mtg.order.domain.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aizen.mtg.order.domain.order.Order;
import ru.aizen.mtg.order.domain.order.OrderRepository;
import ru.aizen.mtg.order.domain.projection.OrderNotFoundException;

import java.util.Collection;

@Component
@SuppressWarnings("unused")
public class OrderQueryHandler {

	private final OrderRepository orderRepository;

	@Autowired
	public OrderQueryHandler(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@QueryHandler
	public Collection<Order> handle(ClientOrderListQuery query) {
		return orderRepository.findAllByClientId(query.getClientId());
	}

	@QueryHandler
	public Collection<Order> handle(TraderOrderListQuery query) {
		return orderRepository.findAllByTraderId(query.getTraderId());
	}

	@QueryHandler
	public Order handle(OrderQuery query) {
		return orderRepository.findByOrderId(query.getOrderId())
				.orElseThrow(() -> new OrderNotFoundException(query.getOrderId()));
	}
}
