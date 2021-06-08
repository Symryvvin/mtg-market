package ru.aizen.mtg.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmOrder {

	private final long orderId;

}
