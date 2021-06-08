package ru.aizen.mtg.order.application.rest.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.order.application.rest.CartResource;
import ru.aizen.mtg.order.domain.cart.Cart;
import ru.aizen.mtg.order.domain.single.Single;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SingleDTO extends RepresentationModel<SingleDTO> {

	private final String id;
	private final String info;
	private final double price;
	private final int quantity;

	public static SingleDTO from(Cart cart, Single single, int quantity) {
		SingleDTO dto = new SingleDTO(single.id(), single.info(), single.price(), quantity);

		dto.add(linkTo(methodOn(CartResource.class).remove(cart.id(), single.id())).withRel("remove"));
		dto.add(linkTo(methodOn(CartResource.class).increase(cart.id(), single.id())).withRel("increase"));
		dto.add(linkTo(methodOn(CartResource.class).decrease(cart.id(), single.id())).withRel("decrease"));

		return dto;
	}

}
