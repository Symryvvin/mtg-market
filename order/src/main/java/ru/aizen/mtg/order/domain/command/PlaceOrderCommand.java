package ru.aizen.mtg.order.domain.command;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import ru.aizen.mtg.order.domain.order.OrderItem;
import ru.aizen.mtg.order.domain.order.OrderStatus;

import java.util.Collection;

@Data
public class PlaceOrderCommand {

	@TargetAggregateIdentifier
	private final String orderId;
	private final OrderStatus status = OrderStatus.PLACED;
	private final long clientId;
	private final String storeId;
	private final Collection<OrderItem> items;

}
