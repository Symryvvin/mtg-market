package ru.aizen.mtg.store.application.resource.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.store.application.resource.SingleResource;
import ru.aizen.mtg.store.application.resource.dto.request.CreateSingleDTO;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.store.Store;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor
public class SinglePresentation extends RepresentationModel<SinglePresentation> {

	private final String singleId;
	private final String oracleName;
	private final String name;
	private final String setCode;
	private final String langCode;
	private final String style;
	private final String condition;
	private final double price;
	private final int inStock;

	public static SinglePresentation of(Single single, Store store) {
		SinglePresentation dto = new SinglePresentation(
				single.id(),
				single.oracleName(),
				single.name(),
				single.setCode(),
				single.langCode(),
				single.style().name(),
				single.condition().getValue(),
				single.price(),
				single.inStock()
		);
		dto.add(linkTo(methodOn(SingleResource.class)
				.editSingleInStore(store.trader().id(), single.id(), new CreateSingleDTO())).withRel("edit"));
		dto.add(linkTo(methodOn(SingleResource.class)
				.deleteSingleFromStore(store.trader().id(), single.id())).withRel("delete"));
		return dto;
	}

	public static SinglePresentation view(Single single) {
		return new SinglePresentation(
				single.id(),
				single.oracleName(),
				single.name(),
				single.setCode(),
				single.langCode(),
				single.style().name(),
				single.condition().getValue(),
				single.price(),
				single.inStock()
		);
	}

}
