package ru.aizen.mtg.application.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateStoreDTO {

	private final long userId;
	private final String username;
	private final String userLocation;

}