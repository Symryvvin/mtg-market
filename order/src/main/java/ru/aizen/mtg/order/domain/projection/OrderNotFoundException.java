package ru.aizen.mtg.order.domain.projection;

public class OrderNotFoundException extends RuntimeException {

	public OrderNotFoundException(String orderId) {
		super("Заказ с ID [" + orderId + "] не найден");
	}
}
