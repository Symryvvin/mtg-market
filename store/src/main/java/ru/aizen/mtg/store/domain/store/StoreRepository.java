package ru.aizen.mtg.store.domain.store;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface StoreRepository extends MongoRepository<Store, String> {

	@Query(value = "{ 'singles.oracleId' : ?0}")
	Collection<Store> findAllBySinglesOracleId(String oracleId);

	Optional<Store> findByTraderId(long traderId);

	Optional<Store> findByTraderName(String traderName);

	boolean existsByTraderId(long traderId);

}