package ru.aizen.mtg.order.domain.command;

import lombok.Getter;
import ru.aizen.mtg.order.domain.order.OrderItem;

import java.util.Collection;

@Getter
public class PlaceOrderCommand extends OrderCommand {

	private final long clientId;
	private final long traderId;
	private final Collection<OrderItem> items;

	public PlaceOrderCommand(String orderId, long clientId, long traderId, Collection<OrderItem> items) {
		super(orderId);
		this.clientId = clientId;
		this.traderId = traderId;
		this.items = items;
	}
}
