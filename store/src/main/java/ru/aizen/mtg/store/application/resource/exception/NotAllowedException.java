package ru.aizen.mtg.store.application.resource.exception;

public class NotAllowedException extends RuntimeException {

	public NotAllowedException() {
		super("Operation not allowed");
	}
}
