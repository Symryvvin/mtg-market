package ru.aizen.mtg.order.application.command;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class OrderCommand {

	private final LocalDateTime creationDate = LocalDateTime.now();
	private final long userId;

	public OrderCommand(long userId) {
		this.userId = userId;
	}

}
