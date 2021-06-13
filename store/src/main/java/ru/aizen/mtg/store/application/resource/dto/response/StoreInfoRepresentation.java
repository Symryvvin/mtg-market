package ru.aizen.mtg.store.application.resource.dto.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.store.application.resource.StoreResource;
import ru.aizen.mtg.store.domain.store.Store;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class StoreInfoRepresentation extends RepresentationModel<StoreInfoRepresentation> {

	private String traderName;
	private int singlesTotal;

	public static StoreInfoRepresentation from(Store store) {
		StoreInfoRepresentation model = new StoreInfoRepresentation(
				store.trader().name(),
				store.singles().size()
		);

		model.add(linkTo(methodOn(StoreResource.class).view(store.trader().name())).withSelfRel());

		return model;
	}

}
