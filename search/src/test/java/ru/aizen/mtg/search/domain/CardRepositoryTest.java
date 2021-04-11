package ru.aizen.mtg.search.domain;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.aizen.mtg.search.domain.card.Card;
import ru.aizen.mtg.search.domain.card.CardRepository;

import java.util.UUID;

@SpringBootTest
public class CardRepositoryTest {

//	@Autowired
//	private CardRepository cardRepository;
//
//	@Test
//	public void save() throws Exception {
//		Card card = Card.from(
//				UUID.randomUUID().toString(),
//				"test",
//				"test",
//				"ccc",
//				"en"
//		);
//		cardRepository.save(card);
//	}
//
//	@Test
//	public void select() throws Exception {
//		Iterable<Card> cards = cardRepository.findAll();
//
//		cards.forEach(System.out::println);
//	}

}
