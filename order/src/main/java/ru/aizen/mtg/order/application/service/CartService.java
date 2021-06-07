package ru.aizen.mtg.order.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.aizen.mtg.order.domain.cart.Cart;
import ru.aizen.mtg.order.domain.cart.CartNotFoundException;
import ru.aizen.mtg.order.domain.cart.CartRepository;
import ru.aizen.mtg.order.domain.single.Single;
import ru.aizen.mtg.order.domain.trader.Trader;

import java.util.Collection;
import java.util.Optional;

@Service
public class CartService {

	@Value("${service.store.uri}")
	private String storeResource;

	private final CartRepository cartRepository;
	private final RestTemplate restTemplate = new RestTemplate();

	@Autowired
	public CartService(CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}

	public Collection<Cart> viewUserCarts(long userId) {
		return cartRepository.findAllByClientId(userId);
	}

	public void addToUserCart(long userId, String storeId, String singleId) {
		ResponseEntity<SingleResponse> response;
		try {
			response = restTemplate.getForEntity(
					storeResource + "/store/{storeId}/single/{singleId}",
					SingleResponse.class,
					storeId,
					singleId
			);
		} catch (RestClientException e) {
			throw new CartServiceException("Can`t add single to cart");
		}

		SingleResponse body = response.getBody();
		if (response.getStatusCode() == HttpStatus.OK && body != null) {
			Trader trader = new Trader(body.getTraderId(), body.getTrader());

			Optional<Cart> optionalCart = cartRepository.findByClientIdAndTraderId(userId, trader.id());
			Cart cart = optionalCart.orElseGet(() -> new Cart(userId, trader));

			Single single = new Single(body.getSingleId(), body.getInfo(), body.getPrice());

			cart.add(single);
			cartRepository.save(cart);
		} else {
			throw new CartServiceException("Can`t add single to cart");
		}
	}

	public void removeFromUserCart(String cartId, String singleId) {
		Optional<Cart> optionalCart = cartRepository.findById(cartId);
		if (optionalCart.isPresent()) {
			Cart cart = optionalCart.get();
			cart.remove(singleId);
			cartRepository.save(cart);
		} else {
			throw new CartNotFoundException(cartId);
		}
	}

	public void increaseSingleCountInUserCart(String cartId, String singleId) {
		Optional<Cart> optionalCart = cartRepository.findById(cartId);
		if (optionalCart.isPresent()) {
			Cart cart = optionalCart.get();
			cart.increase(singleId);
			cartRepository.save(cart);
		} else {
			throw new CartNotFoundException(cartId);
		}
	}

	public void decreaseSingleCountInUserCart(String cartId, String singleId) {
		Optional<Cart> optionalCart = cartRepository.findById(cartId);
		if (optionalCart.isPresent()) {
			Cart cart = optionalCart.get();
			cart.decrease(singleId);
			cartRepository.save(cart);
		} else {
			throw new CartNotFoundException(cartId);
		}
	}

	public void clearCart(String cartId) {
		Optional<Cart> optionalCart = cartRepository.findById(cartId);
		if (optionalCart.isPresent()) {
			Cart cart = optionalCart.get();
			cart.removeAll();
			cartRepository.save(cart);
		} else {
			throw new CartNotFoundException(cartId);
		}
	}

}
