package ru.aizen.mtg.order.application.rest.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.order.domain.trader.Trader;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TraderDTO extends RepresentationModel<TraderDTO> {

	private final long id;
	private final String name;

	public static TraderDTO from(Trader trader) {
		return new TraderDTO(trader.id(), trader.name());
	}

}
