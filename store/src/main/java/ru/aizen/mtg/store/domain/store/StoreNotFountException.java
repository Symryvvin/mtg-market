package ru.aizen.mtg.store.domain.store;

public class StoreNotFountException extends RuntimeException {

	public StoreNotFountException(String name, String owner) {
		super(owner + "'s store '" + name + "' not found");
	}

	public StoreNotFountException(String storeId) {
		super("Store with id '" + storeId + "' not found");
	}
}