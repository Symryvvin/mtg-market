package ru.aizen.mtg.domain.account.security;

public class PasswordSecureException extends Exception {

	public PasswordSecureException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
}
