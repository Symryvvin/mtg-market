package ru.aizen.mtg.order.domain.command;

import lombok.Getter;

@Getter
public class RemoveOrderItemCommand extends OrderCommand {

	private final String itemId;
	private final String item;

	public RemoveOrderItemCommand(String orderId, String itemId, String item) {
		super(orderId);
		this.itemId = itemId;
		this.item = item;
	}
}
