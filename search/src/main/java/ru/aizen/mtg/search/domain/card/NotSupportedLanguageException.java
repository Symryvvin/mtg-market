package ru.aizen.mtg.search.domain.card;

import org.springframework.util.StringUtils;

public class NotSupportedLanguageException extends Exception {

	public NotSupportedLanguageException(String languageCode) {
		super(message(languageCode));
	}

	private static String message(String languageCode) {
		if (StringUtils.hasLength(languageCode)) {
			return "Try to parse Language by code, but code value is empty";
		} else {
			return String.format("Language code [%s] is not support", languageCode);
		}
	}

}
