package ru.aizen.mtg.store.application.resource.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.aizen.mtg.store.application.resource.StoreResource;
import ru.aizen.mtg.store.application.resource.dto.CreateSingleDTO;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.store.Store;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor
public class SingleDTO extends RepresentationModel<SingleDTO> {

	private final String singleId;
	private final String oracleId;
	private final String oracleName;
	private final String name;
	private final String setCode;
	private final String langCode;
	private final String style;
	private final String condition;
	private final double price;
	private final int inStock;

	public static SingleDTO of(Single single, Store store) {
		SingleDTO dto = new SingleDTO(
				single.id(),
				single.oracleId(),
				single.oracleName(),
				single.name(),
				single.setCode(),
				single.langCode(),
				single.style().name(),
				single.condition().getValue(),
				single.price(),
				single.inStock()
		);
		dto.add(linkTo(methodOn(StoreResource.class).editSingleInStore(store.id(), store.owner().id(), single.id(), new CreateSingleDTO())).withRel("edit"));
		dto.add(linkTo(methodOn(StoreResource.class).deleteSingleFromStore(store.id(), store.owner().id(), single.id())).withRel("delete"));
		return dto;
	}

	public static SingleDTO view(Single single) {
		return new SingleDTO(
				single.id(),
				single.oracleId(),
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
