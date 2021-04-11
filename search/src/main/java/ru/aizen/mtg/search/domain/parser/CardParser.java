package ru.aizen.mtg.search.domain.parser;

import ru.aizen.mtg.search.domain.card.Card;

import java.nio.file.Path;
import java.util.Collection;

public interface CardParser {

	Collection<Card> parseCardFrom(Path path) throws CardParserException;

}
