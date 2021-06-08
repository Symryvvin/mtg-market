package ru.aizen.mtg.order.application.rest.response;

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
public class CartDTO extends RepresentationModel<CartDTO> {

	private final TraderDTO trader;
	private final List<SingleDTO> singles;

	public static CartDTO from(Cart cart) {
		TraderDTO traderDTO = TraderDTO.from(cart.trader());
		List<SingleDTO> singlesDTO = new ArrayList<>();
		cart.singles().stream()
				.collect(Collectors.groupingBy(Single::id))
				.forEach((singleId, list) -> {
					singlesDTO.add(SingleDTO.from(cart, list.get(0), list.size()));
				});

		CartDTO dto = new CartDTO(traderDTO, singlesDTO);
		dto.add(linkTo(methodOn(CartResource.class).clear(cart.id())).withRel("clear"));

		return dto;
	}

}
