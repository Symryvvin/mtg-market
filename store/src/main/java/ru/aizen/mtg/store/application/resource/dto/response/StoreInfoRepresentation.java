package ru.aizen.mtg.store.application.resource.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.store.application.resource.StoreResource;
import ru.aizen.mtg.store.domain.store.Store;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreInfoRepresentation extends RepresentationModel<StoreInfoRepresentation> {

	private final String traderName;
	private final int singlesTotal;

	public static StoreInfoRepresentation from(Store store) {
		StoreInfoRepresentation model = new StoreInfoRepresentation(
				store.owner().name(),
				store.singles().size()
		);

		model.add(linkTo(methodOn(StoreResource.class).find(store.id())).withSelfRel());

		return model;
	}

}
