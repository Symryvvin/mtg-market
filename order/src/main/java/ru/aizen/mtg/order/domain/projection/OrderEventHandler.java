package ru.aizen.mtg.order.domain.projection;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aizen.mtg.order.domain.event.OrderCanceledEvent;
import ru.aizen.mtg.order.domain.event.OrderCompletedEvent;
import ru.aizen.mtg.order.domain.event.OrderConfirmedEvent;
import ru.aizen.mtg.order.domain.event.OrderPlacedEvent;
import ru.aizen.mtg.order.domain.order.Order;
import ru.aizen.mtg.order.domain.order.OrderRepository;

@Component
@SuppressWarnings("unused")
public class OrderEventHandler {

	private final OrderRepository orderRepository;

	@Autowired
	public OrderEventHandler(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@EventHandler
	public void orderPlacedEventHandler(OrderPlacedEvent event) {
		Order order = Order.create(event.getOrderId(),
				event.getOrderNumber(),
				event.getClientId(),
				event.getStoreId(),
				event.getStatus(),
				event.getItems()
		);
		if (orderRepository.findByOrderId(event.getOrderId()).isEmpty()) {
			orderRepository.save(order);
		}
	}

	@EventHandler
	public void orderConfirmedEventHandler(OrderConfirmedEvent event) {
		Order order = findOrder(event.getOrderId());
		order.status(event.getStatus());
	}

	@EventHandler
	public void orderCanceledEventHandler(OrderCanceledEvent event) {
		Order order = findOrder(event.getOrderId());
		order.status(event.getStatus());
	}

	@EventHandler
	public void orderCompletedEventHandler(OrderCompletedEvent event) {
		Order order = findOrder(event.getOrderId());
		order.status(event.getStatus());
	}

	private Order findOrder(String orderId) {
		return orderRepository.findByOrderId(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
	}
}
