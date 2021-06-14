package ru.aizen.mtg.search.infrastructure.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.aizen.mtg.search.domain.card.Card;

import java.io.IOException;

public class CardDeserializer extends JsonDeserializer<Card> {

	@Override
	public Card deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		return Card.from(node.get("id").textValue(),
				node.get("oracle_id").textValue(),
				node.get("name").textValue(),
				node.get("printed_name") == null ? node.get("name").textValue() : node.get("printed_name").textValue(),
				node.get("set").textValue(),
				node.get("lang").textValue());

	}
}
