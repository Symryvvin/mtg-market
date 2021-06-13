package ru.aizen.mtg.order.application.resource.response.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import ru.aizen.mtg.order.application.resource.OrderResource;
import ru.aizen.mtg.order.domain.order.Order;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor
@Relation(collectionRelation = "orders", itemRelation = "order")
public class OrderPresentation extends RepresentationModel<OrderPresentation> {

	private final String orderId;
	private final String orderNumber;
	private final LocalDateTime creationDate;
	private final String status;
	private final String totalCost;
	private final int itemCount;

	public static OrderPresentation from(Order order) {
		double totalCost = order.items().stream()
				.mapToDouble(item -> item.getPrice() * item.getQuantity())
				.sum() + order.shippingCost();

		var model = new OrderPresentation(
				order.orderId(),
				order.orderNumber(),
				order.creationDate(),
				order.status().getMessage(),
				String.format("%1$,.2f", totalCost),
				order.items().size()
		);

		model.add(linkTo(methodOn(OrderResource.class).view(order.orderId())).withSelfRel());

		return model;
	}

}
