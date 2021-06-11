package ru.aizen.mtg.order.domain.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrderStatus {

	PLACED("Заказ создан"),
	CONFIRMED("Заказ подтвержден"),
	PAID("Заказ оплачен"),
	CANCELED("Заказ отменен"),
	COMPLETED("Заказ завершен");

	private final String message;

}
