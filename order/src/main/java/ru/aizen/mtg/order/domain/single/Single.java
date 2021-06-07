package ru.aizen.mtg.order.domain.single;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

@Accessors(fluent = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@Document
public class Single {

	private String id;
	private String info;
	private double price;

}
