package ru.aizen.mtg.order.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import ru.aizen.mtg.order.domain.order.OrderStatus;

@Getter
@AllArgsConstructor
public class ConfirmOrderCommand {

	@TargetAggregateIdentifier
	private final String orderId;
	private final OrderStatus status = OrderStatus.CONFIRMED;

}
