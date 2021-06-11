package ru.aizen.mtg.order.application.resource.response.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.order.domain.event.OrderEvent;
import ru.aizen.mtg.order.domain.order.Order;
import ru.aizen.mtg.order.domain.order.OrderItem;
import ru.aizen.mtg.order.domain.order.OrderStatus;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderPresentation extends RepresentationModel<OrderPresentation> {

	private final String orderId;
	private final String orderNumber;
	private final long clientId;
	private final String storeId;
	private final OrderStatus status;
	private final Collection<OrderItem> items;
	private final double shippingCost;
	private final String shippedTo;

	private final Collection<EventLog> eventLogs;

	public static OrderPresentation from(Order order, Collection<OrderEvent> events) {
		return new OrderPresentation(
				order.orderId(),
				order.orderNumber(),
				order.clientId(),
				order.storeId(),
				order.status(),
				order.items(),
				order.shippingCost(),
				order.shippedTo(),
				events.stream().map(EventLog::from).collect(Collectors.toList())
		);
	}

}
