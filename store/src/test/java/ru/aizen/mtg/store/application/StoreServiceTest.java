package ru.aizen.mtg.store.application;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.aizen.mtg.store.domain.single.Condition;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.single.Style;
import ru.aizen.mtg.store.domain.store.Store;
import ru.aizen.mtg.store.domain.store.StoreException;
import ru.aizen.mtg.store.domain.store.StoreRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreServiceTest {

	private static final String ORACLE_ID = "06e77ee5-b20d-4add-9b3f-4af2da3ba27f";

	@Autowired
	private StoreRepository repository;
	private StoreService service;

	private Store store;

	@BeforeEach
	void beforeEach() {
		service = new StoreService(repository);
		Store testStore = Store.create(-1, "Test Username", "Test City", "MyStore");
		Single testSingle = Single.create(
				ORACLE_ID, "testSingleName")
				.name("Name")
				.setCode("CC")
				.langCode("us")
				.style(Style.REGULAR)
				.condition(Condition.M)
				.inStock(2)
				.price(1.5);
		testStore.add(testSingle);

		store = repository.save(testStore);
	}

	@AfterEach
	void afterEach() {
		repository.delete(store);
	}

	@Test
	void createStore() {
		Store store = service.createStore(1, "Username", "Краснодар", "MyStore");

		repository.findById(store.id()).ifPresentOrElse(
				s -> {
					assertEquals("Username", s.owner().name());
					assertEquals("Краснодар", s.owner().location());
					assertEquals("MyStore", s.name());
				},
				() -> Assertions.fail("Store with id " + store.id() + " not found")
		);

		repository.delete(store);
	}

	@Test
	void blockStore() {
		service.blockStore(store.id());
		repository.findById(store.id()).ifPresentOrElse(
				s -> assertTrue(s.blocked()),
				() -> Assertions.fail("Store with id " + store.id() + " not found")
		);

		service.unblockStore(store.id());
		repository.findById(store.id()).ifPresentOrElse(
				s -> assertFalse(s.blocked()),
				() -> Assertions.fail("Store with id " + store.id() + " not found")
		);
	}

	@Test
	void removeStore() {
		service.removeStore(store.id());

		assertFalse(repository.findById(store.id()).isPresent());
	}

	@Test
	void stores() {
		long userId = -999;
		Store store1 = repository.save(Store.create(userId, "John Doe", "Краснодар", "MyStore1"));
		Store store2 = repository.save(Store.create(userId, "John Doe", "Краснодар", "MyStore2"));

		Collection<Store> storesByOwnerName = service.stores("John Doe");
		Collection<Store> storesByOwnerId = service.stores(userId);

		assertEquals(2, storesByOwnerName.size());
		assertEquals(2, storesByOwnerId.size());

		repository.delete(store1);
		repository.delete(store2);
	}

	@Test
	void addSingle() {
		service.addSingle(store.id(), Single.create(
				UUID.randomUUID().toString(), "oracleName")
				.name("name")
				.setCode("code")
				.langCode("lang")
				.style(Style.REGULAR)
				.condition(Condition.M)
				.inStock(1)
				.price(0.25));

		repository.findById(store.id()).ifPresentOrElse(
				s -> assertEquals(2, s.singles().size()),
				() -> Assertions.fail("Store with id " + store.id() + " not found")
		);
	}

	@Test
	void addSingles() {
		Collection<Single> singles = Arrays.asList(
				Single.create(UUID.randomUUID().toString(), "oracleName")
						.name("name")
						.setCode("code")
						.langCode("lang")
						.style(Style.REGULAR)
						.condition(Condition.M)
						.inStock(1)
						.price(0.25),
				Single.create(UUID.randomUUID().toString(), "oracleName")
						.name("name")
						.setCode("code")
						.langCode("lang")
						.style(Style.FOIL)
						.condition(Condition.M)
						.inStock(1)
						.price(0.25));

		service.addSingles(store.id(), singles);

		repository.findById(store.id()).ifPresentOrElse(
				s -> assertEquals(2, s.singles().size()),
				() -> Assertions.fail("Store with id " + store.id() + " not found")
		);
	}

	@Test
	void editSingle() throws Exception {
		Single single = Single.create(
				UUID.randomUUID().toString(), "oracleName")
				.name("name")
				.setCode("code")
				.langCode("lang")
				.style(Style.REGULAR)
				.condition(Condition.M)
				.inStock(1)
				.price(0.25);

		service.addSingle(store.id(), single);

		service.editSingle(store.id(), single.id(),
				"new_name", "code", "lang", "FOIL", "M", single.price(), single.inStock());

		repository.findById(store.id()).ifPresentOrElse(
				s -> {
					try {
						Single updatedSingle = s.findSingleById(single.id());
						assertEquals("new_name", updatedSingle.name());
					} catch (StoreException e) {
						Assertions.fail("Single with id " + single.id() + " not found");
					}
				},
				() -> Assertions.fail("Store with id " + store.id() + " not found")
		);
	}

	@Test
	void deleteSingle() {
		store.singles().stream().findAny().ifPresentOrElse(
				single -> {
					service.deleteSingle(store.id(), single.id());

					repository.findById(store.id()).ifPresentOrElse(
							s -> assertThrows(StoreException.class, () -> s.findSingleById(single.id())),
							() -> Assertions.fail("Store with id " + store.id() + " not found")
					);
				},
				() -> Assertions.fail("No singles in store")
		);
	}

	@Test
	void reserveSingle() throws Exception {
		Single single = Single.create(
				UUID.randomUUID().toString(), "oracleName")
				.name("name")
				.setCode("code")
				.langCode("lang")
				.style(Style.REGULAR)
				.condition(Condition.M)
				.inStock(2)
				.price(0.25);

		service.addSingle(store.id(), single);
		service.reserveSingle(store.id(), single.id(), 2);

		repository.findById(store.id()).ifPresentOrElse(
				s -> {
					try {
						assertTrue(s.findSingleById(single.id()).isReserved());
					} catch (StoreException e) {
						Assertions.fail("Single with id " + single.id() + " not found");
					}
				},
				() -> Assertions.fail("Store with id " + store.id() + " not found")
		);
	}

	@Test
	void findInStoresByCardId() {
		assertEquals(1, service.findInStoresBySingleId(ORACLE_ID).size());
	}
}