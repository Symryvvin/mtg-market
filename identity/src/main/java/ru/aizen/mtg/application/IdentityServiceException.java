package ru.aizen.mtg.application;

public class IdentityServiceException extends RuntimeException {

	public IdentityServiceException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

	public IdentityServiceException(String message) {
		super(message);
	}

}
