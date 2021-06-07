package ru.aizen.mtg.order.application.rest.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.order.application.rest.TraderResource;
import ru.aizen.mtg.order.domain.trader.Trader;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TraderDTO extends RepresentationModel<TraderDTO> {

	private final long id;
	private final String name;

	public static TraderDTO from(Trader trader) {
		TraderDTO dto = new TraderDTO(trader.id(), trader.name());

		dto.add(linkTo(methodOn(TraderResource.class).trader(trader.id())).withSelfRel());

		return dto;
	}

}
