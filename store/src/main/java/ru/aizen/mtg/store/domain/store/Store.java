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

@Accessors(fluent = true)
@Getter
@Setter
@ToString
@Document(collection = "card_store")
public class Store {

	@Id
	private String id;
	private boolean blocked;
	private final String name;
	private final StoreOwner owner;
	private Collection<Single> singles;

	private LocalDateTime creationTime;
	private LocalDateTime updateTime;

	private Store(StoreOwner owner, String name) {
		this.owner = owner;
		this.name = name;
		this.singles = new ArrayList<>();
		this.creationTime = LocalDateTime.now();
		this.updateTime = creationTime;
	}

	public static Store create(long userId, String username, String userLocation, String name) {
		StoreOwner owner = new StoreOwner(userId, username, userLocation);
		return new Store(owner, name);
	}

	public Single findSingleById(String singleId) throws StoreException {
		return singles.stream()
				.filter(single -> single.id().equalsIgnoreCase(singleId))
				.findFirst()
				.orElseThrow(() -> new StoreException("Single with id " + singleId + " not found in store " + name));

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
		this.singles = new ArrayList<>(singles);
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
