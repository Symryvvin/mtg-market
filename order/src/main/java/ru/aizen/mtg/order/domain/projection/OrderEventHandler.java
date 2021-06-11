package ru.aizen.mtg.order.domain.projection;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aizen.mtg.order.domain.event.*;
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
		orderRepository.deleteByOrderId(event.getOrderId());

		Order order = Order.create(event.getOrderId(),
				generateOrderNumber(event.getOrderId(), event.getClientId(), event.getTraderId()),
				event.getClientId(),
				event.getTraderId(),
				event.getStatus(),
				event.getEventTime(),
				event.getItems()
		);
		orderRepository.save(order);
	}

	private String generateOrderNumber(String orderId, long clientId, long traderId) {
		return (orderId.substring(0, 4) + "-" + String.format("%05d", clientId) + "-" + String.format("%05d", traderId))
				.toUpperCase();
	}

	@EventHandler
	public void orderChangeShippingAddressEventHandler(OrderShippingAddressChangedEvent event) {
		Order order = findOrder(event.getOrderId());
		order.shippedTo(event.getShippingAddress());
		orderRepository.save(order);
	}

	@EventHandler
	public void orderChangeShippingCostEventHandler(OrderShippingCostChangedEvent event) {
		Order order = findOrder(event.getOrderId());
		order.shippingCost(event.getShippingCost());
		orderRepository.save(order);
	}

	@EventHandler
	public void orderRemoveItemEventHandler(OrderRemoveItemEvent event) {
		Order order = findOrder(event.getOrderId());
		order.removeItem(event.getItemId());
		orderRepository.save(order);
	}


	@EventHandler
	public void orderConfirmedEventHandler(OrderConfirmedEvent event) {
		Order order = findOrder(event.getOrderId());
		order.status(event.getStatus());
		orderRepository.save(order);
	}

	@EventHandler
	public void orderPaidEventHandler(OrderPaidEvent event) {
		Order order = findOrder(event.getOrderId());
		order.status(event.getStatus());
		orderRepository.save(order);
	}

	@EventHandler
	public void orderCanceledEventHandler(OrderCanceledEvent event) {
		Order order = findOrder(event.getOrderId());
		order.status(event.getStatus());
		orderRepository.save(order);
	}

	@EventHandler
	public void orderCompletedEventHandler(OrderCompletedEvent event) {
		Order order = findOrder(event.getOrderId());
		order.status(event.getStatus());
		orderRepository.save(order);
	}

	private Order findOrder(String orderId) {
		return orderRepository.findByOrderId(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
	}
}
