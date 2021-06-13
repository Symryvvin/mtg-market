package ru.aizen.mtg.store.application.resource.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.store.application.resource.SingleResource;
import ru.aizen.mtg.store.application.resource.StoreResource;
import ru.aizen.mtg.store.application.resource.dto.request.CreateSingleDTO;
import ru.aizen.mtg.store.domain.store.Store;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor
public class StorePresentation extends RepresentationModel<StorePresentation> {

	String id;
	String location;
	String trader;
	List<SinglePresentation> singles;

	public static StorePresentation of(Store store) {
		long traderId = store.trader().id();

		StorePresentation dto = new StorePresentation(
				store.id(),
				store.trader().location(),
				store.trader().name(),
				store.singles().stream()
						.map(single -> SinglePresentation.of(single, store))
						.collect(Collectors.toList()));
		dto.add(linkTo(methodOn(SingleResource.class).addSingleToStore(traderId, new CreateSingleDTO())).withRel("add"));
		dto.add(linkTo(methodOn(SingleResource.class).importSinglesToStore(traderId, null)).withRel("import"));
		dto.add(linkTo(methodOn(StoreResource.class).blockStore(traderId)).withRel("block"));
		dto.add(linkTo(methodOn(StoreResource.class).unblockStore(traderId)).withRel("unblock"));
		return dto;
	}

	public static StorePresentation view(Store store) {
		return new StorePresentation(
				store.id(),
				store.trader().location(),
				store.trader().name(),
				store.singles().stream()
						.map(SinglePresentation::view)
						.collect(Collectors.toList()));
	}

}
