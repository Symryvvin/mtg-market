package ru.aizen.mtg.order.domain.command;

import lombok.Data;
import ru.aizen.mtg.order.domain.order.OrderStatus;

@Data
public class CompleteOrderCommand {

	private final String orderId;
	private final OrderStatus status = OrderStatus.COMPLETED;

}
