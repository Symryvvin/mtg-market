package ru.aizen.mtg.order.domain.event;

import lombok.Getter;

@Getter
public class OrderShippingAddressChangedEvent extends OrderEvent {

	private final String shippingAddress;

	OrderShippingAddressChangedEvent(String orderId, String shippingAddress) {
		super(orderId, null, "Адрес доставки изменен: " + shippingAddress);
		this.shippingAddress = shippingAddress;
	}
}
