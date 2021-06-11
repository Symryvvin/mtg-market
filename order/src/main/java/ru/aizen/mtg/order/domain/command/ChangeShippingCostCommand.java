package ru.aizen.mtg.order.domain.command;

import lombok.Getter;

@Getter
public class ChangeShippingCostCommand extends OrderCommand {

	private final double shippingCost;

	public ChangeShippingCostCommand(String orderId, double shippingCost) {
		super(orderId);
		this.shippingCost = shippingCost;
	}
}
