package ru.aizen.mtg.store.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.aizen.mtg.store.domain.cart.Cart;
import ru.aizen.mtg.store.domain.cart.CartItem;
import ru.aizen.mtg.store.domain.cart.CartNotFoundException;
import ru.aizen.mtg.store.domain.cart.CartRepository;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.single.SingleNotFoundException;
import ru.aizen.mtg.store.domain.store.Store;
import ru.aizen.mtg.store.domain.store.StoreNotFountException;
import ru.aizen.mtg.store.domain.store.StoreRepository;

import java.util.stream.Collectors;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final StoreRepository storeRepository;
	private final RestTemplate restTemplate;
	private final String orderService;

	@Autowired
	public CartService(CartRepository cartRepository,
	                   StoreRepository storeRepository,
	                   @Value("${orderService}") String orderService) {
		this.cartRepository = cartRepository;
		this.storeRepository = storeRepository;
		this.orderService = orderService;
		this.restTemplate = new RestTemplate();
	}

	public Cart clientCart(long clientId) {
		return cartRepository.findByClientId(clientId).orElseGet(() -> Cart.create(clientId));
	}

	public void addToCart(long clientId, long traderId, String singleId) {
		Cart cart = cartRepository.findByClientId(clientId)
				.orElseGet(() -> Cart.create(clientId));

		Store store = storeRepository.findByTraderId(traderId).orElseThrow(StoreNotFountException::new);
		Single single = store.findSingleById(singleId)
				.orElseThrow(() -> new SingleNotFoundException(singleId, store.trader().name()));
		cart.add(traderId, single);
		cartRepository.save(cart);
	}

	public String placeOrder(long clientId, long traderId) {
		Cart cart = cartRepository.findByClientId(clientId).orElseThrow(() -> new CartNotFoundException(clientId));

		try {
			String orderId = restTemplate.postForEntity(orderService + "/rest/order",
					new CreateOrderDTO(
							cart.clientId(),
							traderId,
							cart.items()
									.values()
									.stream()
									.map(ItemDTO::from)
									.collect(Collectors.toList())
					),
					String.class).getBody();

			clearCart(cart, traderId);

			return orderId;
		} catch (RestClientException e) {
			throw new IllegalStateException(e);
		}
	}

	public void removeCartItem(Cart cart, String singleId) {
		cart.remove(singleId);
		cartRepository.save(cart);
	}

	public void increaseCartItemQuantity(Cart cart, String singleId) {
		CartItem item = cart.items().get(singleId);

		if (item != null) {
			storeRepository.findByTraderId(item.traderId())
					.flatMap(store -> store.findSingleById(singleId))
					.ifPresent(single -> {
						if (single.hasInStock() && item.quantity() < single.inStock()) {
							cart.increase(singleId);
							cartRepository.save(cart);
						} else {
							throw new IllegalArgumentException("Нельзя положить в корзину карт больше чем есть у продавца");
						}
					});
		}
	}

	public void decreaseCartItemQuantity(Cart cart, String singleId) {
		cart.decrease(singleId);
		cartRepository.save(cart);
	}

	public void clearCart(Cart cart, long traderId) {
		cart.clear(traderId);
		cartRepository.save(cart);
	}

	public void clearCart(long clientId) {
		cartRepository.deleteByClientId(clientId);
	}


}
