package ru.aizen.mtg.order.application.resource.request;

import lombok.Data;
import ru.aizen.mtg.order.domain.order.OrderItem;

import java.util.Collection;

@Data
public class PlaceOrderDTO {

	private final long clientId;
	private final long traderId;
	private final Collection<OrderItem> items;

}
