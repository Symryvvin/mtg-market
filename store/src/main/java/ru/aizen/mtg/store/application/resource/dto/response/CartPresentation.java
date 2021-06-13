package ru.aizen.mtg.store.application.resource.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import ru.aizen.mtg.store.application.resource.CartResource;
import ru.aizen.mtg.store.domain.cart.Cart;
import ru.aizen.mtg.store.domain.cart.CartItem;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Relation(itemRelation = "cart", collectionRelation = "carts")
public class CartPresentation extends RepresentationModel<CartPresentation> {

	private final long traderId;
	private final Collection<CartItemPresentation> singles;

	public static CartPresentation from(Cart cart, long traderId, List<CartItem> items) {
		CartPresentation model = new CartPresentation(
				traderId,
				items.stream().map(item -> CartItemPresentation.from(cart.clientId(), item)).collect(Collectors.toList())
		);

		model.add(linkTo(methodOn(CartResource.class).clear(cart.clientId())).withRel("clearAll"));
		model.add(linkTo(methodOn(CartResource.class).clearCart(cart.clientId(), traderId)).withRel("clear"));

		return model;
	}

}
