package ru.aizen.mtg.store.domain.cart;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.aizen.mtg.store.domain.single.Single;

@Accessors(fluent = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document
public class CartItem {

	private final long traderId;
	private final String singleId;
	private final String name;
	private final String attributes;
	private final double price;
	private int quantity;

	public static CartItem from(Single single, long traderId) {
		String name = single.oracleName().equalsIgnoreCase(single.name()) ?
				single.oracleName() :
				single.name() + "(" + single.oracleName() + ")";
		String attributes = single.setCode() + " " + single.langCode() + " " + single.style() + " " + single.condition();
		return new CartItem(traderId, single.id(), name, attributes, single.price(), 1);
	}

	public void increaseQuantity() {
		this.quantity++;
	}

	public void decreaseQuantity() {
		if (this.quantity > 1) {
			this.quantity--;
		}
	}

}
