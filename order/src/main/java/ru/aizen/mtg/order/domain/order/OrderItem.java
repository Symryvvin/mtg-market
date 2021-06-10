package ru.aizen.mtg.order.domain.order;

import lombok.Data;

@Data
public class OrderItem {

	private final String singleId;
	private final String name;
	private final String attributes;
	private final double price;
	private int quantity;

}
