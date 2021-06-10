package ru.aizen.mtg.order.domain.event;

import lombok.Data;
import ru.aizen.mtg.order.domain.order.OrderStatus;

@Data
public class OrderCompletedEvent {

	private final String orderId;
	private final OrderStatus status = OrderStatus.COMPLETED;

}
