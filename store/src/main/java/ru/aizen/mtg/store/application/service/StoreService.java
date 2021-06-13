package ru.aizen.mtg.store.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aizen.mtg.store.application.resource.dto.response.FoundSingle;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.single.SingleNotFoundException;
import ru.aizen.mtg.store.domain.store.Store;
import ru.aizen.mtg.store.domain.store.StoreNotFountException;
import ru.aizen.mtg.store.domain.store.StoreRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StoreService {

	private final StoreRepository storeRepository;

	@Autowired
	public StoreService(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}

	public Store create(long traderID, String trader, String traderLocation) {
		if (!storeRepository.existsByTraderId(traderID)) {
			Store store = Store.create(traderID, trader, traderLocation);
			return storeRepository.save(store);
		} else {
			throw new StoreAlreadyExistsException(traderID, trader);
		}
	}

	public Store store(long traderId) {
		return storeRepository.findByTraderId(traderId).orElseThrow(StoreNotFountException::new);
	}

	public Store store(String username) {
		return storeRepository.findByTraderName(username).orElseThrow(StoreNotFountException::new);
	}

	public void blockStore(Store store) {
		store.block();
		storeRepository.save(store);
	}

	public void unblockStore(Store store) {
		store.unblock();
		storeRepository.save(store);
	}

	public void addSingle(Store store, Single single) {
		store.add(single);
		storeRepository.save(store);
	}

	public void addSingles(Store store, Collection<Single> singles) {
		store.add(singles);
		storeRepository.save(store);
	}

	public void editSingle(Store store, String singleId,
	                       String name, String setCode, String langCode, String style,
	                       String conditionValue, double price, int inStock) throws SingleNotFoundException {

		Single single = store.findSingleById(singleId)
				.orElseThrow(() -> new SingleNotFoundException(singleId, store.trader().name()));
		single.printParameters(name, setCode, langCode, style);
		single.tradeParameters(conditionValue, price, inStock);
		store.update(single);
		storeRepository.save(store);
	}

	public void deleteSingle(Store store, String singleId) {
		store.remove(singleId);
		storeRepository.save(store);
	}

	public void reserveSingle(Store store, String singleId, int count) {
		Single single = store.findSingleById(singleId)
				.orElseThrow(() -> new SingleNotFoundException(singleId, store.trader().name()));
		single.reserve(count);
		store.update(single);
		storeRepository.save(store);
	}

	public Collection<FoundSingle> findInStoresBySingleId(String oracleId) {
		return storeRepository.findAllBySinglesOracleId(oracleId).stream()
				.filter(store -> !store.blocked())
				.flatMap(store ->
						store.singles().stream()
								.filter(single -> single.oracleId().equals(oracleId) && !single.isReserved())
								.map(single -> FoundSingle.from(store, single)))
				.collect(Collectors.toList());
	}

}
