package ru.aizen.mtg.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.aizen.mtg.order.domain.single.Single;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class CreateOrderCommand {

	private final long clientId;
	private final long traderId;
	private final Collection<Single> singles;

}
