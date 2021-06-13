package ru.aizen.mtg.store.application.service;

public class StoreAlreadyExistsException extends RuntimeException {

	public StoreAlreadyExistsException(long traderId, String trader) {
		super("Магазин пользователя '" + trader + "' [id '" + traderId + "'] уже существует. " +
				"Каждый пользователь может иметь только один магазин");
	}
}
