package ru.aizen.mtg.store.application.resource.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReserveSingleDTO {

	private String singleId;
	private int count;

}
