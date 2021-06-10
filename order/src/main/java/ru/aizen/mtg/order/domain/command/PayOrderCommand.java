package ru.aizen.mtg.order.domain.command;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import ru.aizen.mtg.order.domain.order.OrderStatus;

@Data
public class PayOrderCommand {

	@TargetAggregateIdentifier
	private final String orderId;
	private final String paymentInfo;
	private final OrderStatus status = OrderStatus.PAID;

}
