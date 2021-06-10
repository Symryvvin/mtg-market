package ru.aizen.mtg.store.application.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aizen.mtg.store.application.resource.dto.response.CartRepresentation;
import ru.aizen.mtg.store.application.service.CartService;
import ru.aizen.mtg.store.domain.cart.Cart;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/rest/cart")
public class CartResource {

	private final CartService cartService;

	@Autowired
	public CartResource(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping(path = "/edit",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<CartRepresentation> edit(@RequestHeader("X-UserId") Long clientId) {
		Collection<CartRepresentation> models = new ArrayList<>();

		Cart cart = cartService.clientCart(clientId);
		cart.groupedByStore().forEach((storeId, items) -> {
			models.add(CartRepresentation.from(cart.id(), storeId, items));
		});
		return CollectionModel.of(models);
	}

	@PutMapping(path = "/add/{storeId}/{singleId}")
	public ResponseEntity<Void> add(@RequestHeader("X-UserId") Long clientId,
	                                @PathVariable("storeId") String storeId,
	                                @PathVariable("singleId") String singleId) {
		cartService.addToCart(clientId, storeId, singleId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/{cardId}/remove/{singleId}")
	public ResponseEntity<Void> remove(@PathVariable("cardId") String cartId,
	                                   @PathVariable("singleId") String singleId) {
		cartService.removeCartItem(cartId, singleId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/{cardId}/increase/{singleId}")
	public ResponseEntity<Void> increase(@PathVariable("cardId") String cartId,
	                                     @PathVariable("singleId") String singleId) {
		cartService.increaseCartItemQuantity(cartId, singleId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/{cardId}/decrease/{singleId}")
	public ResponseEntity<Void> decrease(@PathVariable("cardId") String cartId,
	                                     @PathVariable("singleId") String singleId) {
		cartService.decreaseCartItemQuantity(cartId, singleId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{cardId}/clear")
	public ResponseEntity<Void> clear(@PathVariable("cardId") String cartId) {
		cartService.clearCart(cartId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{cardId}/clear/{storeId}")
	public ResponseEntity<Void> clearStoreCart(@PathVariable("cardId") String cartId,
	                                           @PathVariable("storeId") String storeId) {
		cartService.clearStoreCart(cartId, storeId);
		return ResponseEntity.ok().build();
	}


}
