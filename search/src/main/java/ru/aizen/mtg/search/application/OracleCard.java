package ru.aizen.mtg.search.application;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.aizen.mtg.search.domain.card.Card;

@Getter
@EqualsAndHashCode
public class OracleCard {

	private final String id;
	private final String name;

	public OracleCard(Card card) {
		this.id = card.getOracleId();
		this.name = card.getPrintedName();
	}
}
