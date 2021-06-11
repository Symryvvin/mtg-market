package ru.aizen.mtg.order.domain.order;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

	void deleteByOrderId(String orderId);

	Optional<Order> findByOrderId(String orderId);

	Collection<Order> findAllByClientId(long clientId);

	Collection<Order> findAllByStoreId(String StoreId);

}
