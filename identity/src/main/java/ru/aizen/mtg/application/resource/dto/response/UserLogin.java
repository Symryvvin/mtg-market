package ru.aizen.mtg.application.resource.dto.response;

import lombok.Getter;

@Getter
public class UserLogin {

	private final boolean success;
	private final int status;
	private final String token;
	private final String message;

	public UserLogin(boolean success, int status, String token, String message) {
		this.success = success;
		this.status = status;
		this.token = token;
		this.message = message;
	}
}
