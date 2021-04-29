package ru.aizen.mtg.domain.profile;

import java.util.regex.Pattern;

public class Email {

	private static final Pattern emailPattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

	private final String email;

	public Email(String email) {
		this.email = email;
	}

	public static Email from(String email) {
		email = email.trim();
		if (emailPattern.matcher(email).matches()) {
			return new Email(email);
		} else {
			throw new IllegalArgumentException("Wrong email");
		}
	}

	public String getEmail() {
		return email;
	}

}
