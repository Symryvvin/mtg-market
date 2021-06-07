package ru.aizen.mtg.order.domain.cart;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

	Optional<Cart> findByClientIdAndTraderId(long clientId, long traderId);

	Collection<Cart> findAllByClientId(long clientId);

}
