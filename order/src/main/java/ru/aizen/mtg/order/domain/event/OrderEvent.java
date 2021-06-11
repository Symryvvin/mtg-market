package ru.aizen.mtg.order.domain.event;

import lombok.Data;
import ru.aizen.mtg.order.domain.order.OrderItem;
import ru.aizen.mtg.order.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class OrderEvent {

	private final String orderId;
	private final OrderStatus status;
	private final LocalDateTime eventTime = LocalDateTime.now();
	private final String message;

	public static OrderEvent place(String orderId,
	                               long clientId,
	                               String storeId,
	                               Collection<OrderItem> items) {
		return new OrderPlacedEvent(orderId, clientId, storeId, items);
	}

	public static OrderEvent changeShippingAddress(String orderId, String shippingAddress) {
		return new OrderShippingAddressChangedEvent(orderId, shippingAddress);
	}

	public static OrderEvent changeShippingCost(String orderId, double shippingCost) {
		return new OrderShippingCostChangedEvent(orderId, shippingCost);
	}

	public static OrderEvent removeItem(String orderId, String itemId, String item) {
		return new OrderRemoveItemEvent(orderId, itemId, item);
	}

	public static OrderEvent confirm(String orderId) {
		return new OrderConfirmedEvent(orderId);
	}

	public static OrderEvent pay(String orderId, String paymentInfo) {
		return new OrderPaidEvent(orderId, paymentInfo);
	}

	public static OrderEvent cancel(String orderId) {
		return new OrderCanceledEvent(orderId);
	}

	public static OrderEvent complete(String orderId) {
		return new OrderCompletedEvent(orderId);
	}

}
