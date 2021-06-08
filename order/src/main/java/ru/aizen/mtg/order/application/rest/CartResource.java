package ru.aizen.mtg.order.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aizen.mtg.order.application.rest.request.AddSingleToCartDTO;
import ru.aizen.mtg.order.application.rest.response.CartDTO;
import ru.aizen.mtg.order.application.service.CartService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
public class CartResource {

	private final CartService cartService;

	@Autowired
	public CartResource(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping(path = "/edit",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<CartDTO> edit(@RequestHeader("X-UserId") Long userId) {
		return CollectionModel.of(cartService.viewUserCarts(userId).stream()
				.map(CartDTO::from)
				.collect(Collectors.toList())
		);
	}

	@PutMapping(path = "/add",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> add(@RequestHeader("X-UserId") Long userId, @RequestBody AddSingleToCartDTO single) {
		cartService.addToUserCart(userId, single.getStoreId(), single.getSingleId());
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/{cardId}/remove/{singleId}")
	public ResponseEntity<Void> remove(@PathVariable("cardId") String cartId, @PathVariable("singleId") String singleId) {
		cartService.removeFromUserCart(cartId, singleId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/{cardId}/increase/{singleId}")
	public ResponseEntity<Void> increase(@PathVariable("cardId") String cartId, @PathVariable("singleId")  String singleId) {
		cartService.increaseSingleCountInUserCart(cartId, singleId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/{cardId}/decrease/{singleId}")
	public ResponseEntity<Void> decrease(@PathVariable("cardId") String cartId, @PathVariable("singleId")  String singleId) {
		cartService.decreaseSingleCountInUserCart(cartId, singleId);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{cardId}/clear")
	public ResponseEntity<Void> clear(@PathVariable("cardId") String cartId) {
		cartService.clearCart(cartId);
		return ResponseEntity.ok().build();
	}


}
