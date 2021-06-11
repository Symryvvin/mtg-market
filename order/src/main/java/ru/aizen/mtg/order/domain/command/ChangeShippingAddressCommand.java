package ru.aizen.mtg.order.domain.command;

import lombok.Getter;

@Getter
public class ChangeShippingAddressCommand extends OrderCommand {

	private final String shippingAddress;

	public ChangeShippingAddressCommand(String orderId, String shippingAddress) {
		super(orderId);
		this.shippingAddress = shippingAddress;
	}

}
