package ru.aizen.mtg.order.domain.event;

import lombok.Data;
import ru.aizen.mtg.order.domain.order.OrderStatus;

@Data
public class OrderPaidEvent {

	private final String orderId;
	private final String paymentInfo;
	private final OrderStatus status = OrderStatus.PAID;

}
