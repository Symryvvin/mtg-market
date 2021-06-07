package ru.aizen.mtg.order.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.aizen.mtg.order.application.rest.request.AddSingleToCartDTO;
import ru.aizen.mtg.order.application.rest.response.CartDTO;
import ru.aizen.mtg.order.application.service.CartService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cart")
public class CartResource {

	private final CartService cartService;

	@Autowired
	public CartResource(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping(path = "/view",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<CartDTO> view(@RequestHeader("X-UserId") Long userId) {
		return cartService.viewUserCarts(userId).stream()
				.map(CartDTO::from)
				.collect(Collectors.toList());
	}

	@PutMapping(path = "/add",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void add(@RequestHeader("X-UserId") Long userId, @RequestBody AddSingleToCartDTO single) {
		cartService.addToUserCart(userId, single.getStoreId(), single.getSingleId());
	}

	@PutMapping(path = "/{cardId}/remove",
			consumes = MediaType.TEXT_PLAIN_VALUE)
	public void remove(@PathVariable("cardId") String cartId, @RequestBody String singleId) {
		cartService.removeFromUserCart(cartId, singleId);
	}

	@PutMapping(path = "/{cardId}/increase",
			consumes = MediaType.TEXT_PLAIN_VALUE)
	public void increase(@PathVariable("cardId") String cartId, @RequestBody String singleId) {
		cartService.increaseSingleCountInUserCart(cartId, singleId);
	}

	@PutMapping(path = "/{cardId}/decrease",
			consumes = MediaType.TEXT_PLAIN_VALUE)
	public void decrease(@PathVariable("cardId") String cartId, @RequestBody String singleId) {
		cartService.decreaseSingleCountInUserCart(cartId, singleId);
	}

	@PutMapping("/{cardId}/clear")
	public void clear(@PathVariable("cardId") String cartId) {
		cartService.clearCart(cartId);
	}


}
