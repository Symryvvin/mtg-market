package ru.aizen.mtg.order.domain.query;

import lombok.Data;

@Data
public class TraderOrderListQuery implements OrderListQuery{

	private final long traderId;

	@Override
	public Long userId() {
		return traderId;
	}
}
