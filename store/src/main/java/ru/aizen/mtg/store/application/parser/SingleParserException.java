package ru.aizen.mtg.store.application.parser;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Parse IO Errors")
public class SingleParserException extends RuntimeException {

	public SingleParserException(String message, Throwable cause) {
		super(message, cause);
	}
}
