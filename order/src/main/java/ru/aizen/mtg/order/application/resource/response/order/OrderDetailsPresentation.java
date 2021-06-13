package ru.aizen.mtg.order.application.resource.response.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.order.application.resource.OrderResource;
import ru.aizen.mtg.order.domain.event.OrderEvent;
import ru.aizen.mtg.order.domain.order.Order;
import ru.aizen.mtg.order.domain.order.OrderItem;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor
public class OrderDetailsPresentation extends RepresentationModel<OrderDetailsPresentation> {

	private final String orderId;
	private final String orderNumber;
	private final long clientId;
	private final long traderId;
	private final String status;
	private final Collection<OrderItem> items;
	private final double shippingCost;
	private final String shippedTo;
	private final String totalCost;

	private final Collection<EventLog> eventLogs;

	public static OrderDetailsPresentation from(Order order, Collection<OrderEvent> events) {
		double totalCost = order.items().stream()
				.mapToDouble(item -> item.getPrice() * item.getQuantity())
				.sum() + order.shippingCost();

		var model = new OrderDetailsPresentation(
				order.orderId(),
				order.orderNumber(),
				order.clientId(),
				order.traderId(),
				order.status().getMessage(),
				order.items(),
				order.shippingCost(),
				order.shippedTo(),
				String.format("%1$,.2f", totalCost),
				events.stream().map(EventLog::from).collect(Collectors.toList())
		);

		model.add(linkTo(methodOn(OrderResource.class).changeShippingAddress(order.orderId(), ""))
				.withRel("change_shipping_address"));
		model.add(linkTo(methodOn(OrderResource.class).changeShippingCost(order.orderId(), ""))
				.withRel("change_shipping_cost"));
		model.add(linkTo(methodOn(OrderResource.class).removeItem(order.orderId(), ""))
				.withRel("remove_item"));

		model.add(linkTo(methodOn(OrderResource.class).cancel(order.orderId()))
				.withRel("cancel"));
		model.add(nexPossibleOrderStatusLink(order));

		return model;
	}

	private static Link nexPossibleOrderStatusLink(Order order) {
		switch (order.status()) {
			case PLACED:
				return linkTo(methodOn(OrderResource.class).confirm(order.orderId())).withRel("next_status");
			case CONFIRMED:
				return linkTo(methodOn(OrderResource.class).pay(order.orderId(), "")).withRel("next_status");
			case PAID:
				return linkTo(methodOn(OrderResource.class).complete(order.orderId())).withRel("next_status");
			default:
				return linkTo(methodOn(OrderResource.class).cancel(order.orderId())).withRel("next_status");
		}
	}

}
