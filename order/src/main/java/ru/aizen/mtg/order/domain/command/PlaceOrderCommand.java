package ru.aizen.mtg.order.domain.command;

import lombok.Getter;
import ru.aizen.mtg.order.domain.order.OrderItem;

import java.util.Collection;

@Getter
public class PlaceOrderCommand extends OrderCommand {

	private final long clientId;
	private final String storeId;
	private final Collection<OrderItem> items;

	public PlaceOrderCommand(String orderId, long clientId, String storeId, Collection<OrderItem> items) {
		super(orderId);
		this.clientId = clientId;
		this.storeId = storeId;
		this.items = items;
	}
}
