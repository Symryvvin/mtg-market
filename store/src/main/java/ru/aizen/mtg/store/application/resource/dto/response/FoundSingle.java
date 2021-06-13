package ru.aizen.mtg.store.application.resource.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import ru.aizen.mtg.store.application.resource.CartResource;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.store.Store;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@AllArgsConstructor
@Relation(itemRelation = "single", collectionRelation = "singles")
public class FoundSingle extends RepresentationModel<FoundSingle> {

	private final String oracleName;

	private final String name;
	private final String setCode;
	private final String langCode;
	private final String style;

	private final String storeId;
	private final String singleId;
	private final String ownerName;
	private final String ownerLocation;

	private final String condition;
	private final double price;
	private final int inStock;

	public static FoundSingle from(Store store, Single single) {
		var model = new FoundSingle(
				single.oracleName(),
				single.name(),
				single.setCode(),
				single.langCode(),
				single.style().name(),
				store.id(),
				single.id(),
				store.trader().name(),
				store.trader().location(),
				single.condition().getValue(),
				single.price(),
				single.inStock()
		);

		model.add(linkTo(methodOn(CartResource.class)
				.add(null, store.trader().id(), single.id())).withRel("addToCart"));

		return model;
	}

}
