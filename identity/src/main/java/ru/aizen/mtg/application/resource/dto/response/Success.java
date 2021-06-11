package ru.aizen.mtg.application.resource.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Success {

	private final boolean success = true;
	private final int status;
	private final String message;

	public Success(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public static Success OK(String message) {
		return new Success(HttpStatus.OK.value(), message);
	}
}
