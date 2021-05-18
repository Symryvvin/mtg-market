package ru.aizen.mtg.apigateway.filter.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class TokenData {

	private final long userId;
	private final String username;
	private final String userRole;
	private final String userLocation;
	private final boolean blocked;

	public static TokenData of(String userId, Map<String, Object> claims) {
		return new TokenData(
				Long.parseLong(userId),
				(String) claims.get("username"),
				(String) claims.get("role"),
				(String) claims.get("location"),
				(Boolean) claims.get("isBlocked")
		);
	}

}
