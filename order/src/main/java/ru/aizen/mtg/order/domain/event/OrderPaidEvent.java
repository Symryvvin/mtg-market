package ru.aizen.mtg.order.domain.event;

import lombok.Getter;
import ru.aizen.mtg.order.domain.order.OrderStatus;

@Getter
public class OrderPaidEvent extends OrderEvent {

	private final String paymentInfo;

	OrderPaidEvent(String orderId, String paymentInfo) {
		super(orderId, OrderStatus.PAID, paymentInfo);
		this.paymentInfo = paymentInfo;
	}
}
