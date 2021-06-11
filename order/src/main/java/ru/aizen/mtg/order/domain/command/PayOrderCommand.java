package ru.aizen.mtg.order.domain.command;

import lombok.Getter;

@Getter
public class PayOrderCommand extends OrderCommand {

	private final String paymentInfo;

	public PayOrderCommand(String orderId, String paymentInfo) {
		super(orderId);
		this.paymentInfo = paymentInfo;
	}

}
