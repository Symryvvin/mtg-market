package ru.aizen.mtg.order.application.rest.response.representation.model.cart;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.order.application.rest.CartResource;
import ru.aizen.mtg.order.domain.cart.Cart;
import ru.aizen.mtg.order.domain.single.Single;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartModel extends RepresentationModel<CartModel> {

	private final long traderId;
	private final List<SingleModel> singles;

	public static CartModel from(Cart cart) {
		List<SingleModel> singlesDTO = new ArrayList<>();
		cart.singles().stream()
				.collect(Collectors.groupingBy(Single::id))
				.forEach((singleId, list) -> {
					singlesDTO.add(SingleModel.from(cart, list.get(0), list.size()));
				});

		CartModel dto = new CartModel(cart.traderId(), singlesDTO);
		dto.add(linkTo(methodOn(CartResource.class).clear(cart.id())).withRel("clear"));

		return dto;
	}

}
