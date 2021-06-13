package ru.aizen.mtg.store.domain.store;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.aizen.mtg.store.domain.single.Single;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Accessors(fluent = true)
@Getter
@Setter
@ToString
@Document(collection = "store")
public class Store {

	@Id
	private String id;
	private boolean blocked;
	private final Trader trader;
	private Collection<Single> singles;

	private LocalDateTime creationTime;
	private LocalDateTime updateTime;

	private Store(Trader trader) {
		this.trader = trader;
		this.singles = new ArrayList<>();
		this.creationTime = LocalDateTime.now();
		this.updateTime = creationTime;
	}

	public static Store create(long userId, String username, String userLocation) {
		Trader trader = new Trader(userId, username, userLocation);
		return new Store(trader);
	}

	public Optional<Single> findSingleById(String singleId) {
		return singles.stream()
				.filter(single -> single.id().equalsIgnoreCase(singleId))
				.findFirst();
	}

	public int inStock(String singleId) {
		return singles.stream()
				.filter(single -> single.id().equalsIgnoreCase(singleId))
				.map(Single::inStock)
				.findFirst()
				.orElse(0);
	}

	public void block() {
		this.blocked = true;
	}

	public void unblock() {
		this.blocked = false;
	}

	public void add(Single single) {
		this.singles.add(single);
		this.updateTime = LocalDateTime.now();
	}

	public void add(Collection<Single> singles) {
		if (singles == null || singles.isEmpty()) {
			return;
		}
		this.singles.addAll(singles);
		this.updateTime = LocalDateTime.now();
	}

	public void update(Single single) {
		singles.stream()
				.filter(c -> c.id().equals(single.id()))
				.findFirst()
				.ifPresent(c -> c = single);
		this.updateTime = LocalDateTime.now();
	}

	public void remove(String singleId) {
		singles.stream()
				.filter(c -> c.id().equals(singleId))
				.findFirst()
				.ifPresent(c -> singles.remove(c));
		this.updateTime = LocalDateTime.now();
	}

}
