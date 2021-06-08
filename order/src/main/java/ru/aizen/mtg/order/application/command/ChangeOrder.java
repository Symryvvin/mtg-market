package ru.aizen.mtg.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.aizen.mtg.order.domain.single.Single;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class ChangeOrder {

	private final Collection<Single> singlesToAdd;
	private final Collection<Single> singlesToDelete;
	private final double shippingCost;
	private final String shippedTo;

}
