package ru.aizen.mtg.search.infrastructure.parser;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;
import ru.aizen.mtg.search.domain.card.Card;
import ru.aizen.mtg.search.domain.parser.CardParser;
import ru.aizen.mtg.search.domain.parser.CardParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@Component
public class ScryfallCardParser implements CardParser {

	@Override
	public Collection<Card> parseCardFrom(Path path) throws CardParserException {
		try {
			ObjectMapper mapper = configuredMapper();
			JavaType type = mapper.getTypeFactory().constructCollectionType(Collection.class, Card.class);

			return mapper.readValue(Files.newInputStream(path), type);
		} catch (IOException e) {
			throw new CardParserException("", e);
		}
	}

	private ObjectMapper configuredMapper() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Card.class, new CardDeserializer());
		mapper.registerModule(module);
		return mapper;
	}

}
