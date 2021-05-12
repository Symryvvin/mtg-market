package ru.aizen.mtg.store.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.single.SingleNotFoundException;
import ru.aizen.mtg.store.domain.store.Store;
import ru.aizen.mtg.store.domain.store.StoreNotFountException;
import ru.aizen.mtg.store.domain.store.StoreRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {

	private final StoreRepository storeRepository;

	@Autowired
	public StoreService(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}

	public Store create(long userId, String username, String userLocation, String name) {
		Store store = Store.create(userId, username, userLocation, name);
		return storeRepository.save(store);
	}

	public Store view(String owner, String name) {
		return storeRepository.findByNameAndOwnerName(name, owner).orElseThrow(() -> new StoreNotFountException(name, owner));
	}

	public void blockStore(String storeId) {
		storeRepository.findById(storeId)
				.ifPresent(store -> {
					store.block();
					storeRepository.save(store);
				});
	}

	public void unblockStore(String storeId) {
		storeRepository.findById(storeId)
				.ifPresent(store -> {
					store.unblock();
					storeRepository.save(store);
				});
	}

	public void removeStore(String storeId) {
		storeRepository.deleteById(storeId);
	}

	public Collection<Store> stores(long userId) {
		return storeRepository.findAllByOwnerId(userId);
	}

	public Collection<Store> stores(String username) {
		return storeRepository.findAllByOwnerName(username);
	}

	public void addSingle(String storeId, Single single) {
		storeRepository.findById(storeId)
				.ifPresent(store -> {
					store.add(single);
					storeRepository.save(store);
				});
	}

	public void addSingles(String storeId, Collection<Single> singles) {
		storeRepository.findById(storeId)
				.ifPresent(store -> {
					store.add(singles);
					storeRepository.save(store);
				});
	}

	public void editSingle(String storeId, String singleId,
	                       String name, String setCode, String langCode, String style,
	                       String conditionValue, double price, int inStock) throws SingleNotFoundException {
		Optional<Store> storeOptional = storeRepository.findById(storeId);
		if (storeOptional.isPresent()) {
			Store store = storeOptional.get();
			Single single = store.findSingleById(singleId).orElseThrow(() -> new SingleNotFoundException(singleId, store.name()));
			single.printParameters(name, setCode, langCode, style);
			single.tradeParameters(conditionValue, price, inStock);
			store.update(single);
			storeRepository.save(store);
		}
	}

	public void deleteSingle(String storeId, String singleId) {
		storeRepository.findById(storeId)
				.ifPresent(store -> {
					store.remove(singleId);
					storeRepository.save(store);
				});
	}

	public void reserveSingle(String storeId, String singleId, int count) {
		Optional<Store> storeOptional = storeRepository.findById(storeId);
		if (storeOptional.isPresent()) {
			Store store = storeOptional.get();
			Single single = store.findSingleById(singleId).orElseThrow(() -> new SingleNotFoundException(singleId, store.name()));
			single.reserve(count);
			store.update(single);
			storeRepository.save(store);
		}
	}

	public Collection<FoundCard> findInStoresBySingleId(String oracleId) {
		return storeRepository.findAllBySinglesOracleId(oracleId).stream()
				.flatMap(store ->
						store.singles().stream()
								.filter(single -> single.oracleId().equals(oracleId))
								.map(single -> FoundCard.from(store, single)))
				.collect(Collectors.toList());
	}

}