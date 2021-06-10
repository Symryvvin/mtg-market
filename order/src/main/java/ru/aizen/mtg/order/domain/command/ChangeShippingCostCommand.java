package ru.aizen.mtg.order.domain.command;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class ChangeShippingCostCommand {

	@TargetAggregateIdentifier
	private final String orderId;
	private final double shippingCost;

}
