package ru.aizen.mtg.order.domain.command;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class RemoveOrderItemCommand {

	@TargetAggregateIdentifier
	private final String orderId;
	private final String itemId;

}
