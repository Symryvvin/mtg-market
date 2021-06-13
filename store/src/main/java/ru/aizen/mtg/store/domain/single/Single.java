package ru.aizen.mtg.store.domain.single;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Accessors(fluent = true)
@Getter
@Setter
@ToString
@Document
public class Single {

	private String id;
	private final String oracleId;
	private final String oracleName;

	private String name;
	private String setCode;
	private String langCode;
	private Style style;

	private Condition condition;
	private double price;
	private int inStock;

	public Single(String oracleId,
	              String oracleName) {
		this.id = UUID.randomUUID().toString();
		this.oracleId = oracleId;
		this.oracleName = oracleName;
	}

	public static Single create(String oracleId, String oracleName) {
		return new Single(oracleId, oracleName);
	}

	public Single printParameters(String name, String setCode, String langCode, String style) {
		this.name = name;
		this.setCode = setCode;
		this.langCode = langCode;
		this.style = Style.valueOf(style.toUpperCase());
		return this;
	}

	public Single tradeParameters(String condition, double price, int inStock) {
		this.condition = Condition.from(condition);
		this.price = price;
		this.inStock = inStock;
		return this;
	}

	public void reserve(int count) {
		if (this.inStock < count) {
			throw new IllegalArgumentException("Нельзя зарезервировать карт больше чем есть у продавца");
		}

		this.inStock -= count;
	}

	public boolean isReserved() {
		return this.inStock == 0;
	}

}
