package ru.aizen.mtg.store.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.aizen.mtg.store.application.service.StoreService;
import ru.aizen.mtg.store.domain.single.Condition;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.single.Style;
import ru.aizen.mtg.store.domain.store.Store;
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

	private Store testStore;

	@BeforeEach
	void beforeEach() {
		service = new StoreService(repository);
		Store testStore = Store.create(-1, "Test Username", "Test City");
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

		this.testStore = repository.save(testStore);
	}

	@AfterEach
	void afterEach() {
		repository.delete(testStore);
	}

	@Test
	void create() {
		Store store = service.create(1, "Username", "Краснодар");

		repository.findById(store.id()).ifPresentOrElse(
				s -> {
					assertEquals("Username", s.trader().name());
					assertEquals("Краснодар", s.trader().location());
				},
				() -> Assertions.fail("Store with id " + store.id() + " not found")
		);

		repository.delete(store);
	}

	@Test
	void view() {
		Store store = service.store("Test Username");

		assertEquals("Test Username", store.trader().name());
		assertEquals("Test City", store.trader().location());
	}

	@Test
	void blockStore() {
		Store store = service.store(testStore.trader().id());

		service.blockStore(store);
		assertTrue(store.blocked());

		service.unblockStore(store);
		assertFalse(store.blocked());
	}

	@Test
	void addSingle() {
		Store store = service.store(testStore.trader().id());

		service.addSingle(store, Single.create(
				UUID.randomUUID().toString(), "oracleName")
				.name("name")
				.setCode("code")
				.langCode("lang")
				.style(Style.REGULAR)
				.condition(Condition.M)
				.inStock(1)
				.price(0.25));


		assertEquals(2, store.singles().size());
	}

	@Test
	void addSingles() {
		Store store = service.store(testStore.trader().id());

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

		service.addSingles(store, singles);

		assertEquals(3, store.singles().size());
	}

	@Test
	void editSingle() {
		Store store = service.store(testStore.trader().id());

		Single single = Single.create(
				UUID.randomUUID().toString(), "oracleName")
				.name("name")
				.setCode("code")
				.langCode("lang")
				.style(Style.REGULAR)
				.condition(Condition.M)
				.inStock(1)
				.price(0.25);

		service.addSingle(store, single);

		service.editSingle(store, single.id(),
				"new_name", "code", "lang", "FOIL", "M", single.price(), single.inStock());

		store.findSingleById(single.id()).ifPresentOrElse(
				updatedSingle -> assertEquals("new_name", updatedSingle.name()),
				() -> Assertions.fail("Single with id " + single.id() + " not found"));
	}

	@Test
	void deleteSingle() {
		Store store = service.store(testStore.trader().id());

		store.singles().stream().findAny().ifPresentOrElse(
				single -> {
					service.deleteSingle(store, single.id());

					repository.findByTraderId(testStore.trader().id()).ifPresentOrElse(
							s -> assertFalse(s.findSingleById(single.id()).isPresent()),
							() -> Assertions.fail("Store with id " + testStore.id() + " not found")
					);
				},
				() -> Assertions.fail("No singles in store")
		);
	}

	@Test
	void reserveSingle() {
		Store store = service.store(testStore.trader().id());

		Single single = Single.create(
				UUID.randomUUID().toString(), "oracleName")
				.name("name")
				.setCode("code")
				.langCode("lang")
				.style(Style.REGULAR)
				.condition(Condition.M)
				.inStock(2)
				.price(0.25);

		service.addSingle(store, single);
		service.reserveSingle(store, single.id(), 2);

		repository.findByTraderId(store.trader().id()).ifPresentOrElse(
				s -> s.findSingleById(single.id()).ifPresentOrElse(
						updatedSingle -> assertTrue(updatedSingle.isReserved()),
						() -> Assertions.fail("Single with id " + single.id() + " not found")
				),
				() -> Assertions.fail("Store with id " + testStore.id() + " not found")
		);
	}

	@Test
	void findInStoresByCardId() {
		assertEquals(1, service.findInStoresBySingleId(ORACLE_ID).size());
	}
}