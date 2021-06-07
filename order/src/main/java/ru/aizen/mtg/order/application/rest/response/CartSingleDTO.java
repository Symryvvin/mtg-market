package ru.aizen.mtg.order.application.rest.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.aizen.mtg.order.domain.single.Single;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartSingleDTO {

	private final String id;
	private final String info;
	private final double price;
	private final int quantity;

	public static CartSingleDTO from(Single single, int quantity) {
		return new CartSingleDTO(single.id(), single.info(), single.price(), quantity);
	}

}
