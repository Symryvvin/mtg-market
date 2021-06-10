package ru.aizen.mtg.order.application.resource.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Success {

	private final boolean success = true;
	private final int status;
	private final String message;

	private Success(HttpStatus status, String message) {
		this.status = status.value();
		this.message = message;
	}

	public static Success OK(String message) {
		return new Success(HttpStatus.OK, message);
	}

}
