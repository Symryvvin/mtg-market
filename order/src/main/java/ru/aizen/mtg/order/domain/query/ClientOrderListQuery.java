package ru.aizen.mtg.order.domain.query;

import lombok.Data;

@Data
public class ClientOrderListQuery implements OrderListQuery{

	private final long clientId;

	@Override
	public Long userId() {
		return clientId;
	}
}
