package ru.aizen.mtg.store.application.resource.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.store.application.resource.CartResource;
import ru.aizen.mtg.store.domain.cart.CartItem;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItemPresentation extends RepresentationModel<CartPresentation> {

	private final String singleId;
	private final String oracleName;
	private final String name;
	private final String setCode;
	private final String attributes;
	private final double price;
	private final int quantity;

	public static CartItemPresentation from(long clientId, CartItem item) {
		CartItemPresentation model = new CartItemPresentation(
				item.singleId(),
				item.oracleName(),
				item.name(),
				item.setCode(),
				item.attributes(),
				item.price(),
				item.quantity());

		model.add(linkTo(methodOn(CartResource.class).remove(clientId, item.singleId())).withRel("remove"));
		model.add(linkTo(methodOn(CartResource.class).increase(clientId, item.singleId())).withRel("increase"));
		model.add(linkTo(methodOn(CartResource.class).decrease(clientId, item.singleId())).withRel("decrease"));

		return model;
	}

}
