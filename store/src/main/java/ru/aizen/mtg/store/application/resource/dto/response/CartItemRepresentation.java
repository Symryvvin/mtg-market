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
public class CartItemRepresentation extends RepresentationModel<CartRepresentation> {

	private final String singleId;
	private final String name;
	private final String attributes;
	private final double price;
	private final int quantity;

	public static CartItemRepresentation from(String cartId, CartItem item) {
		CartItemRepresentation model = new CartItemRepresentation(item.singleId(),
				item.name(),
				item.attributes(),
				item.price(),
				item.quantity());

		model.add(linkTo(methodOn(CartResource.class).remove(cartId, item.singleId())).withRel("remove"));
		model.add(linkTo(methodOn(CartResource.class).increase(cartId, item.singleId())).withRel("increase"));
		model.add(linkTo(methodOn(CartResource.class).decrease(cartId, item.singleId())).withRel("decrease"));

		return model;
	}

}
