package ru.aizen.mtg.store.application.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.aizen.mtg.store.domain.cart.CartItem;

@Data
@AllArgsConstructor
public class ItemDTO {

	private final String singleId;
	private final String name;
	private final String setCode;
	private final String attributes;
	private final double price;
	private int quantity;

	public static ItemDTO from(CartItem item) {
		return new ItemDTO(item.singleId(), item.name(), item.setCode(), item.attributes(), item.price(), item.quantity());
	}
}
