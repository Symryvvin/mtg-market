package ru.aizen.mtg.order.application.rest.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.aizen.mtg.order.domain.cart.Cart;
import ru.aizen.mtg.order.domain.single.Single;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartDTO {

	private final TraderDTO trader;
	private final List<CartSingleDTO> singles;

	public static CartDTO from(Cart cart) {
		TraderDTO traderDTO = TraderDTO.from(cart.trader());
		List<CartSingleDTO> singlesDTO = new ArrayList<>();
		cart.singles().stream()
				.collect(Collectors.groupingBy(Single::id))
				.forEach((singleId, list) -> {
					singlesDTO.add(CartSingleDTO.from(list.get(0), list.size()));
				});

		return new CartDTO(traderDTO, singlesDTO);
	}

}
