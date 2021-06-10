package ru.aizen.mtg.store.application.resource.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.store.application.resource.CartResource;
import ru.aizen.mtg.store.domain.cart.CartItem;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartRepresentation extends RepresentationModel<CartRepresentation> {

	private final String storeId;
	private final Collection<CartItemRepresentation> singles;

	public static CartRepresentation from(String cartId, String storeId, List<CartItem> items) {
		CartRepresentation model = new CartRepresentation(
				storeId,
				items.stream().map(item -> CartItemRepresentation.from(cartId, item)).collect(Collectors.toList())
		);

		model.add(linkTo(methodOn(CartResource.class).clear(cartId)).withRel("clearAll"));
		model.add(linkTo(methodOn(CartResource.class).clearStoreCart(cartId, storeId)).withRel("clear"));

		return model;
	}

}
