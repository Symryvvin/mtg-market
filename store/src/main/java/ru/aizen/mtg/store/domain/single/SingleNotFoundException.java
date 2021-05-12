package ru.aizen.mtg.store.domain.single;

public class SingleNotFoundException extends RuntimeException {

	public SingleNotFoundException(String singleId, String storeName) {
		super("Single with id " + singleId + " not found in store " + storeName);
	}

}
