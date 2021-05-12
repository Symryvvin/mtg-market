package ru.aizen.mtg.store.application.resource.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreateSingleDTO {

	private String oracleId;
	private String oracleName;
	private String name;
	private String setCode;
	private String langCode;
	private String style;
	private String condition;
	private double price;
	private int inStock;

}
