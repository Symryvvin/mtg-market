package ru.aizen.mtg.store.domain.single;

public class SingleNotFoundException extends RuntimeException {

	public SingleNotFoundException(String singleId, String storeId) {
		super("Карта с ID [" + singleId + "] не найдена в магазине с ID [" + storeId + "]");
	}

}
