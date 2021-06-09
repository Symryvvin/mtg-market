package ru.aizen.mtg.order.domain.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.aizen.mtg.order.domain.single.Single;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Accessors(fluent = true)
@Getter
@Setter
@ToString
@Document(collection = "cart")
public class Cart {

	@Id
	private String id;
	private final long clientId;
	private final long traderId;
	private Collection<Single> singles;

	public Cart(long clientId, long traderId) {
		this.singles = new ArrayList<>();
		this.clientId = clientId;
		this.traderId = traderId;
	}

	public void add(Single single) {
		singles.add(single);
	}

	public void remove(String singleId) {
		Collection<Single> toRemove = singles.stream().filter(single -> single.id().equalsIgnoreCase(singleId))
				.collect(Collectors.toList());
		singles.removeAll(toRemove);
	}

	public void increase(String singleId) {
		singles.stream()
				.filter(single -> single.id().equalsIgnoreCase(singleId))
				.findFirst()
				.ifPresent(singles::add);
	}

	public void decrease(String singleId) {
		LinkedList<Single> toRemove = singles.stream()
				.filter(single -> single.id().equalsIgnoreCase(singleId))
				.collect(Collectors.toCollection(LinkedList::new));
		if (toRemove.size() > 1) {
			singles.remove(toRemove.getFirst());
		}
	}

}
