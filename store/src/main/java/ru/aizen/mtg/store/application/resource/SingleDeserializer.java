package ru.aizen.mtg.store.application.resource;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.aizen.mtg.store.domain.single.Condition;
import ru.aizen.mtg.store.domain.single.Single;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

public class SingleDeserializer extends JsonDeserializer<Single> {
	@Override
	public Single deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException {
		ObjectCodec codec = jsonParser.getCodec();
		JsonNode node = codec.readTree(jsonParser);

		Random random = new Random();
		String condition = Condition.values()[random.nextInt(Condition.values().length)].getValue();
		double price = 0.25 + (100 - 0.25) * random.nextDouble();
		DecimalFormat df = new DecimalFormat("#.##");
		price = Double.parseDouble(df.format(price));
		int inStock = random.nextInt(7 - 1) + 1;

		return Single.create(node.get("oracleId").asText(), node.get("oracleName").asText())
				.printParameters(node.get("printedName").asText(),
						node.get("setCode").asText(),
						node.get("language").asText(),
						"regular")
				.tradeParameters(condition, price, inStock);
	}
}
