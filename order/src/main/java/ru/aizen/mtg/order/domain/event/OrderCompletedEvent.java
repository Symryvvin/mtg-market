package ru.aizen.mtg.order.domain.event;

import lombok.Getter;
import ru.aizen.mtg.order.domain.order.OrderStatus;

@Getter
public class OrderCompletedEvent extends OrderEvent {

	OrderCompletedEvent(String orderId) {
		super(orderId, OrderStatus.COMPLETED, null);
	}

}
