package ru.aizen.mtg.domain.account.security;

import java.util.Arrays;

public enum Role {

	CLIENT,
	TRADER,
	MANAGER,
	UNDEFINED;

	public static Role find(String role) {
		return Arrays.stream(Role.values())
				.filter(r -> r.name().equalsIgnoreCase(role))
				.findFirst()
				.orElse(UNDEFINED);
	}
}
