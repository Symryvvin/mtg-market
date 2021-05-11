package ru.aizen.mtg.store.domain.store;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface StoreRepository extends MongoRepository<Store, String> {

	@Query(value = "{ 'singles.oracleId' : ?0}")
	Collection<Store> findAllBySinglesOracleId(String oracleId);

	Collection<Store> findAllByOwnerId(long userId);

	Collection<Store> findAllByOwnerName(String ownerName);

}