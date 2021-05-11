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

	public void changePrintParameters(String name, String setCode, String langCode, String style) {
		this.name = name;
		this.setCode = setCode;
		this.langCode = langCode;
		this.style = Style.valueOf(style.toUpperCase());
	}

	public void changeTradeParameters(String condition, double price, int inStock) {
		this.condition = Condition.from(condition);
		this.price = price;
		this.inStock = inStock;
	}

	public void reserve(int count) {
		if (this.inStock < count) {
			System.out.println("нельзя");
		}

		this.inStock -= count;
	}

	public boolean isReserved() {
		return this.inStock == 0;
	}

}
