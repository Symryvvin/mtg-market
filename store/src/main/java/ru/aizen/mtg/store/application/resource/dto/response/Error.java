package ru.aizen.mtg.store.application.resource.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class Error {

	private final LocalDateTime timestamp = LocalDateTime.now();
	private final boolean success = false;
	private final int status;
	private final String message;

	public Error(HttpStatus status, String message) {
		this.status = status.value();
		this.message = message;
	}

}
