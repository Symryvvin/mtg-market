package ru.aizen.mtg.application.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAddressUpdateDTO {

	private long ownerId;
	private Integer postIndex;
	private String settlement;
	private String street;
	private String building;
	private String apartment;

}
