package ru.aizen.mtg.store.domain.cart;

public class CartNotFoundException extends RuntimeException {

	public CartNotFoundException(long userId) {
		super("Корзина пользователя с ID [" + userId + "] не найдена");
	}

}
