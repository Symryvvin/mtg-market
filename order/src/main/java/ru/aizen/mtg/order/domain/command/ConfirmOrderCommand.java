package ru.aizen.mtg.order.domain.command;

public class ConfirmOrderCommand extends OrderCommand {

	public ConfirmOrderCommand(String orderId) {
		super(orderId);
	}
}
