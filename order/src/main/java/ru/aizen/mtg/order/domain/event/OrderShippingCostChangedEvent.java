package ru.aizen.mtg.order.domain.event;

import lombok.Getter;

@Getter
public class OrderShippingCostChangedEvent extends OrderEvent {

	private final double shippingCost;

	OrderShippingCostChangedEvent(String orderId, double shippingCost) {
		super(orderId, null, "Изменена стоимость доставки: " + shippingCost);
		this.shippingCost = shippingCost;
	}
}
