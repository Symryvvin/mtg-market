package ru.aizen.mtg.application.resource.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdateDTO {

	private long ownerId;
	private String fullName;
	private String email;
	private String phone;

}
