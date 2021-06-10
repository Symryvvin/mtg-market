package ru.aizen.mtg.order.domain.event;

import lombok.Data;
import ru.aizen.mtg.order.domain.order.OrderItem;
import ru.aizen.mtg.order.domain.order.OrderStatus;

import java.util.Collection;

@Data
public class OrderPlacedEvent {

	private final String orderId;
	private final String orderNumber;
	private final OrderStatus status = OrderStatus.PLACED;
	private final long clientId;
	private final String storeId;
	private final Collection<OrderItem> items;

}
