package ru.aizen.mtg.store.domain.store;

public class StoreNotFountException extends RuntimeException {

	public StoreNotFountException(String name, String owner) {
		super(owner + "'s store '" + name + "' not found");
	}

}