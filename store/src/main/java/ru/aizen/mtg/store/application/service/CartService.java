package ru.aizen.mtg.store.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aizen.mtg.store.domain.cart.Cart;
import ru.aizen.mtg.store.domain.cart.CartNotFoundException;
import ru.aizen.mtg.store.domain.cart.CartRepository;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.single.SingleNotFoundException;
import ru.aizen.mtg.store.domain.store.Store;
import ru.aizen.mtg.store.domain.store.StoreNotFountException;
import ru.aizen.mtg.store.domain.store.StoreRepository;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final StoreRepository storeRepository;

	@Autowired
	public CartService(CartRepository cartRepository, StoreRepository storeRepository) {
		this.cartRepository = cartRepository;
		this.storeRepository = storeRepository;
	}

	public Cart clientCart(long clientId) {
		return cartRepository.findByClientId(clientId).orElseThrow(() -> new CartNotFoundException(clientId));
	}

	public void addToCart(long clientId, String storeId, String singleId) {
		Cart cart = cartRepository.findByClientId(clientId)
				.orElseGet(() -> Cart.create(clientId));

		Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFountException::new);
		Single single = store.findSingleById(singleId).orElseThrow(() -> new SingleNotFoundException(singleId, storeId));
		cart.add(storeId, single);
		cartRepository.save(cart);
	}

	private Cart findById(String cartId) {
		return cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
	}

	public void removeCartItem(String cartId, String singleId) {
		Cart cart = findById(cartId);
		cart.remove(singleId);
		cartRepository.save(cart);
	}

	public void increaseCartItemQuantity(String cartId, String singleId) {
		Cart cart = findById(cartId);
		cart.increase(singleId);
		cartRepository.save(cart);
	}

	public void decreaseCartItemQuantity(String cartId, String singleId) {
		Cart cart = findById(cartId);
		cart.decrease(singleId);
		cartRepository.save(cart);
	}

	public void clearStoreCart(String cartId, String storeId) {
		Cart cart = findById(cartId);
		cart.clearForStore(storeId);
		cartRepository.save(cart);
	}

	public void clearCart(String cartId) {
		cartRepository.deleteById(cartId);
	}


}
