package ru.aizen.mtg.store.domain.single;

public class SingleNotFoundException extends RuntimeException {

	public SingleNotFoundException(String singleId, String trader) {
		super("Карта с ID [" + singleId + "] не найдена в магазине пользователя [" + trader + "]");
	}

}
