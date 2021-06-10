package ru.aizen.mtg.store.domain.cart;

public class CartNotFoundException extends RuntimeException {

	public CartNotFoundException(long userId) {
		super("Корзина пользователя с ID [" + userId + "] не найдена");
	}

	public CartNotFoundException(String cartId) {
		super("Корзина с ID [" + cartId + "] не найдена");
	}

}
