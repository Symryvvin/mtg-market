package ru.aizen.mtg.order.domain.event;

import lombok.Getter;
import ru.aizen.mtg.order.domain.order.OrderItem;
import ru.aizen.mtg.order.domain.order.OrderStatus;

import java.util.Collection;

@Getter
public class OrderPlacedEvent extends OrderEvent {

	private final long clientId;
	private final String storeId;
	private final Collection<OrderItem> items;

	OrderPlacedEvent(String orderId,
	                        long clientId,
	                        String storeId,
	                        Collection<OrderItem> items) {
		super(orderId, OrderStatus.PLACED, null);
		this.clientId = clientId;
		this.storeId = storeId;
		this.items = items;
	}
}
