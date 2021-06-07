package ru.aizen.mtg.order.application.rest.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Success {

	private final boolean success = true;
	private final int status;
	private final String message;

	public Success(HttpStatus status, String message) {
		this.status = status.value();
		this.message = message;
	}
}
