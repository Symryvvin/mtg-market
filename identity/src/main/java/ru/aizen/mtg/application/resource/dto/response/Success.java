package ru.aizen.mtg.application.resource.dto.response;

import lombok.Getter;

@Getter
public class Success {

	private final boolean success = true;
	private final int status;
	private final String message;

	public Success(int status, String message) {
		this.status = status;
		this.message = message;
	}
}
