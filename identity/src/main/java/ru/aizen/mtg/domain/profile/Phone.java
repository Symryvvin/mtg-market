package ru.aizen.mtg.domain.profile;

import java.util.regex.Pattern;

public class Phone {

	private static final String PHONE_FORMAT_REGEXP = "(\\d{3})(\\d{3})(\\d{2})(\\d{2})";
	private static final String PHONE_REPLACEMENT = "($1)$2-$3-$4";
	private static final Pattern phoneNumberPattern = Pattern.compile("\\d{10}");
	private static final String SYMBOLS = "[ +-/(/)]*";

	private final long phoneNumber;

	private Phone(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static Phone from(String phoneNumber) {
		if (phoneNumber != null) {
			String trimmed = phoneNumber.replaceAll(SYMBOLS, "");
			if (phoneNumberPattern.matcher(trimmed).matches()) {
				return new Phone(Long.parseLong(trimmed));
			} else {
				throw new IllegalArgumentException("Wrong phone number. Must contain 10 digits and may contains + ( ) - symbols");
			}
		} else {
			return null;
		}
	}

	public static Phone from(Long phoneNumber) {
		if (phoneNumber == null) {
			return null;
		}
		return new Phone(phoneNumber);
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public String getFormattedPhone() {
		return Long.toString(phoneNumber).replaceFirst(PHONE_FORMAT_REGEXP, PHONE_REPLACEMENT);
	}

}
