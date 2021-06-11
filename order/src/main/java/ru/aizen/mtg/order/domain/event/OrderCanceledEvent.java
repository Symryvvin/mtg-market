package ru.aizen.mtg.order.domain.event;

import lombok.Getter;
import ru.aizen.mtg.order.domain.order.OrderStatus;

@Getter
public class OrderCanceledEvent extends OrderEvent {

	OrderCanceledEvent(String orderId) {
		super(orderId, OrderStatus.CANCELED, null);
	}

}
