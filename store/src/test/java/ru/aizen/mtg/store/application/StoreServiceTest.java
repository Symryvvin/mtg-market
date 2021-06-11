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

	private Store store;

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

		store = repository.save(testStore);
	}

	@AfterEach
	void afterEach() {
		repository.delete(store);
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
	void editSingle() {
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
				s -> s.findSingleById(single.id()).ifPresentOrElse(
						updatedSingle -> assertEquals("new_name", updatedSingle.name()),
						() -> Assertions.fail("Single with id " + single.id() + " not found")
				),
				() -> Assertions.fail("Store with id " + store.id() + " not found")
		);
	}

	@Test
	void deleteSingle() {
		store.singles().stream().findAny().ifPresentOrElse(
				single -> {
					service.deleteSingle(store.id(), single.id());

					repository.findById(store.id()).ifPresentOrElse(
							s -> assertFalse(s.findSingleById(single.id()).isPresent()),
							() -> Assertions.fail("Store with id " + store.id() + " not found")
					);
				},
				() -> Assertions.fail("No singles in store")
		);
	}

	@Test
	void reserveSingle() {
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
				s -> s.findSingleById(single.id()).ifPresentOrElse(
						updatedSingle -> assertTrue(updatedSingle.isReserved()),
						() -> Assertions.fail("Single with id " + single.id() + " not found")
				),
				() -> Assertions.fail("Store with id " + store.id() + " not found")
		);
	}

	@Test
	void findInStoresByCardId() {
		assertEquals(1, service.findInStoresBySingleId(ORACLE_ID).size());
	}
}