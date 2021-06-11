package ru.aizen.mtg.store.domain.store;

public class StoreNotFountException extends RuntimeException {

	public StoreNotFountException() {
		super("Магазин не найден");
	}
}