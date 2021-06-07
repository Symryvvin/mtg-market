package ru.aizen.mtg.order.domain.cart;

public class CartNotFoundException extends RuntimeException {

	public CartNotFoundException(long userId) {
		super("Cart for user with id " + userId + " not found");
	}

	public CartNotFoundException(String cartId) {
		super("Cart with id " + cartId + " not found");
	}

}
