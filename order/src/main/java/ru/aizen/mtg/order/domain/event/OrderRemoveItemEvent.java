package ru.aizen.mtg.order.domain.event;

import lombok.Getter;

@Getter
public class OrderRemoveItemEvent extends OrderEvent {

	private final String itemId;

	OrderRemoveItemEvent(String orderId, String itemId, String item) {
		super(orderId, null, "Удалена позиция: " + item);
		this.itemId = itemId;
	}
}
