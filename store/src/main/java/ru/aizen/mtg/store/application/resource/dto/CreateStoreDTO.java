package ru.aizen.mtg.store.application.resource.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreateStoreDTO {

	private long userId;
	private String username;
	private String userLocation;
	private String storeName;

}
