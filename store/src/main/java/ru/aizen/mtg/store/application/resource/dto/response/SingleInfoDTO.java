package ru.aizen.mtg.store.application.resource.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.store.Store;

@Getter
@AllArgsConstructor
public class SingleInfoDTO {

	private final String singleId;
	private final String trader;
	private final long traderId;
	private final String info;
	private final double price;

	public static SingleInfoDTO of(Single single, Store store) {
		return new SingleInfoDTO(
				single.id(),
				store.owner().name(),
				store.owner().id(),
				info(single),
				single.price());
	}

	private static String info(Single single) {
		String fullName = single.name().equalsIgnoreCase(single.oracleName()) ?
				single.oracleName() :
				single.name() + "(" + single.oracleName() + ")";
		return fullName + " " + single.setCode() + " " + single.langCode() + " " + single.condition() + " " + single.style();
	}

}
