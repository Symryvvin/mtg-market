package ru.aizen.mtg.store.domain.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

@Accessors(fluent = true)
@AllArgsConstructor
@Getter
@ToString
@Document
public class Trader {

	private final long id;
	private final String name;
	private final String location;

}
