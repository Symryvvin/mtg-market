package ru.aizen.mtg.store.domain.cart;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

	Optional<Cart> findByClientId(long clientId);

	void deleteByClientId(long clientId);

}
