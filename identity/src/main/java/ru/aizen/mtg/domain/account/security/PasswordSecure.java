package ru.aizen.mtg.domain.account.security;

public interface PasswordSecure {

	String encrypt(String password) throws PasswordSecureException;

	boolean validate(String password, String encryptedPassword) throws PasswordSecureException;
}
