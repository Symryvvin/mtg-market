package ru.aizen.mtg.store.domain.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;
import ru.aizen.mtg.store.domain.single.Single;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Accessors(fluent = true)
@Getter
@Setter
@ToString
@Document(collection = "cart")
public class Cart {

	@Id
	private String id;
	private final long clientId;
	private Map<String, CartItem> items;

	public Cart(long clientId) {
		this.clientId = clientId;
		this.items = new HashMap<>();
	}

	public static Cart create(Long clientId) {
		Assert.notNull(clientId, "Не определен клиент для создания корзины");
		return new Cart(clientId);
	}

	public void add(String storeId, Single single) {
		CartItem item = CartItem.from(single, storeId);
		if (items.containsKey(single.id())) {
			items.get(single.id()).increaseQuantity();
		} else {
			items.put(single.id(), item);
		}
	}

	public void remove(String singleId) {
		items.remove(singleId);
	}

	public void increase(String singleId) {
		if (items.containsKey(singleId)) {
			items.get(singleId).increaseQuantity();
		}
	}

	public void decrease(String singleId) {
		if (items.containsKey(singleId)) {
			items.get(singleId).decreaseQuantity();
		}
	}

	public void clearForStore(String storeId) {
		var copy = items.values().stream()
				.filter(cartItem -> cartItem.storeId().equalsIgnoreCase(storeId))
				.map(CartItem::singleId)
				.collect(Collectors.toList());

		copy.forEach(id -> items.remove(id));
	}

	public Map<String, List<CartItem>> groupedByStore() {
		return items.values().stream()
				.collect(Collectors.groupingBy(CartItem::storeId));
	}

}
