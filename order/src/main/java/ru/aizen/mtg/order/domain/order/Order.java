package ru.aizen.mtg.order.domain.order;

import ru.aizen.mtg.order.domain.single.Single;

import java.util.Collection;

public class Order {

	private long id;

	private long clientId;
	private String clientName;

	private long traderId;
	private String traderName;

	private Collection<Single> singles;
	private double shippingCost;
	private String shippedTo;

}
