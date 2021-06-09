package ru.aizen.mtg.order.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.aizen.mtg.order.application.rest.response.store.StoreSingle;
import ru.aizen.mtg.order.domain.cart.Cart;
import ru.aizen.mtg.order.domain.cart.CartNotFoundException;
import ru.aizen.mtg.order.domain.cart.CartRepository;
import ru.aizen.mtg.order.domain.single.Single;

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


	public Cart findById(String cartId) {
		return cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
	}

	public void addToUserCart(long clientId, String storeId, String singleId) {
		ResponseEntity<StoreSingle> response;
		try {
			response = restTemplate.getForEntity(storeResource + "/store/{storeId}/single/{singleId}",
					StoreSingle.class, storeId, singleId);
			StoreSingle storeSingle = response.getBody();
			if (storeSingle != null) {
				long traderId = storeSingle.getTraderId();

				Cart cart = cartRepository.findByClientIdAndTraderId(clientId, storeSingle.getTraderId())
						.orElseGet(() -> new Cart(clientId, traderId));

				cart.add(new Single(storeSingle.getSingleId(), storeSingle.getInfo(), storeSingle.getPrice()));
				cartRepository.save(cart);
			} else {
				throw new CartServiceException("Response from store is null");
			}

		} catch (RestClientException e) {
			throw new CartServiceException("Problem to get single info from store", e);
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
		cartRepository.findById(cartId);
	}

}
