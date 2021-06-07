package ru.aizen.mtg.order.domain.trader;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

@Accessors(fluent = true)
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Document
public class Trader {

	private final long id;
	private final String name;

}
