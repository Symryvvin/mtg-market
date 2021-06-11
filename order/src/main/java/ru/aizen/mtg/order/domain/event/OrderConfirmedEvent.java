package ru.aizen.mtg.order.domain.event;

import lombok.Getter;
import ru.aizen.mtg.order.domain.order.OrderStatus;

@Getter
public class OrderConfirmedEvent extends OrderEvent {

	OrderConfirmedEvent(String orderId) {
		super(orderId, OrderStatus.CONFIRMED, null);
	}

}
