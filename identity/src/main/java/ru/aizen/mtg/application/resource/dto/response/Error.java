package ru.aizen.mtg.application.resource.dto.response;

import lombok.Getter;

@Getter
public class Error {

	private final int statusCode;
	private final boolean success = false;
	private final String message;

	public Error(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

}
