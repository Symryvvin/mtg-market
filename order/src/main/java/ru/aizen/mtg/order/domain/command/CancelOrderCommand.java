package ru.aizen.mtg.order.domain.command;

public class CancelOrderCommand extends OrderCommand {

	public CancelOrderCommand(String orderId) {
		super(orderId);
	}
}
