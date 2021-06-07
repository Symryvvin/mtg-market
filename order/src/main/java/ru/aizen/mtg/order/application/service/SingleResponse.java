package ru.aizen.mtg.order.application.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SingleResponse {

	private String singleId;
	private String trader;
	private long traderId;
	private String info;
	private double price;

}
