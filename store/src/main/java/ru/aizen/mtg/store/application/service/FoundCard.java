package ru.aizen.mtg.store.application.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.store.Store;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class FoundCard {

	private String oracleName;

	private String name;
	private String setCode;
	private String langCode;
	private String style;

	private String storeId;
	private String productId;
	private String ownerName;
	private String ownerLocation;

	private String condition;
	private double price;
	private int inStock;

	public static FoundCard from(Store store, Single single) {
		return new FoundCard(
				single.oracleName(),
				single.name(),
				single.setCode(),
				single.langCode(),
				single.style().name(),
				store.id(),
				single.id(),
				store.owner().name(),
				store.owner().location(),
				single.condition().getValue(),
				single.price(),
				single.inStock()
		);
	}

}
