package ru.aizen.mtg.store.application.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aizen.mtg.store.application.resource.dto.response.CartPresentation;
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

	@GetMapping(path = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<CartPresentation> edit(@RequestHeader("X-UserId") Long clientId) {
		Collection<CartPresentation> models = new ArrayList<>();

		Cart cart = cartService.clientCart(clientId);
		cart.groupedByStore().forEach((traderId, items) -> {
			models.add(CartPresentation.from(cart, traderId, items));
		});
		return CollectionModel.of(models);
	}

	@PutMapping(path = "/add/{traderId}/{singleId}")
	public ResponseEntity<Void> add(@RequestHeader("X-UserId") Long userId,
	                                @PathVariable("traderId") Long traderId,
	                                @PathVariable("singleId") String singleId) {
		cartService.addToCart(userId, traderId, singleId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/remove/{singleId}")
	public ResponseEntity<Void> remove(@RequestHeader("X-UserId") Long userId,
	                                   @PathVariable("singleId") String singleId) {
		Cart cart = cartService.clientCart(userId);

		cartService.removeCartItem(cart, singleId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/increase/{singleId}")
	public ResponseEntity<Void> increase(@RequestHeader("X-UserId") Long userId,
	                                     @PathVariable("singleId") String singleId) {
		Cart cart = cartService.clientCart(userId);
		cartService.increaseCartItemQuantity(cart, singleId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/decrease/{singleId}")
	public ResponseEntity<Void> decrease(@RequestHeader("X-UserId") Long userId,
	                                     @PathVariable("singleId") String singleId) {
		Cart cart = cartService.clientCart(userId);
		cartService.decreaseCartItemQuantity(cart, singleId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/clear")
	public ResponseEntity<Void> clear(@RequestHeader("X-UserId") Long userId) {
		cartService.clearCart(userId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/clear/{traderId}")
	public ResponseEntity<Void> clearCart(@RequestHeader("X-UserId") Long userId,
	                                      @PathVariable("traderId") long traderId) {
		Cart cart = cartService.clientCart(userId);
		cartService.clearCart(cart, traderId);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/place/order/{traderId}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> placeOrder(@RequestHeader("X-UserId") Long userId,
	                                         @PathVariable("traderId") long traderId) {
		return ResponseEntity.ok(cartService.placeOrder(userId, traderId));
	}


}
