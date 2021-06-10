package ru.aizen.mtg.order.domain.event;

import lombok.Data;
import ru.aizen.mtg.order.domain.order.OrderStatus;

@Data
public class OrderCanceledEvent {

	private final String orderId;
	private final OrderStatus status = OrderStatus.CANCELED;

}
