package ru.aizen.mtg.apigateway.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateStore {

	private final long userId;
	private final String username;
	private final String userLocation;
	private final String storeName;

}
