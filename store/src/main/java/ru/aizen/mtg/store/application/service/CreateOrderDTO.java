package ru.aizen.mtg.store.application.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@AllArgsConstructor
@Data
public class CreateOrderDTO {

	private final long clientId;
	private final long traderId;
	private final Collection<ItemDTO> items;

}
