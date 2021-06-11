package ru.aizen.mtg.store.application.resource.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.store.application.resource.StoreResource;
import ru.aizen.mtg.store.application.resource.dto.request.CreateSingleDTO;
import ru.aizen.mtg.store.domain.store.Store;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor
public class StoreDTO extends RepresentationModel<StoreDTO> {

	String id;
	String location;
	String trader;
	List<SingleDTO> singles;

	public static StoreDTO of(Store store) {
		StoreDTO dto = new StoreDTO(
				store.id(),
				store.trader().location(),
				store.trader().name(),
				store.singles().stream()
						.map(single -> SingleDTO.of(single, store))
						.collect(Collectors.toList()));
		dto.add(linkTo(methodOn(StoreResource.class).addSingleToStore(store.id(), store.trader().id(), new CreateSingleDTO())).withRel("add"));
		dto.add(linkTo(methodOn(StoreResource.class).importSinglesToStore(store.id(), store.trader().id(), null)).withRel("import"));
		dto.add(linkTo(methodOn(StoreResource.class).blockStore(store.id())).withRel("block"));
		dto.add(linkTo(methodOn(StoreResource.class).unblockStore(store.id())).withRel("unblock"));
		dto.add(linkTo(methodOn(StoreResource.class).deleteStore(store.id(), store.trader().id())).withRel("delete"));
		return dto;
	}

	public static StoreDTO view(Store store) {
		return new StoreDTO(
				store.id(),
				store.trader().location(),
				store.trader().name(),
				store.singles().stream()
						.map(SingleDTO::view)
						.collect(Collectors.toList()));
	}

}
