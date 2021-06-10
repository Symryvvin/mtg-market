package ru.aizen.mtg.store.application.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.web.multipart.MultipartFile;
import ru.aizen.mtg.store.domain.single.Single;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public class JsonSingleParser {

	public Collection<Single> singles(MultipartFile file) {
		try (InputStream inputStream = file.getInputStream()) {
			CollectionType singles = TypeFactory.defaultInstance().constructCollectionType(List.class, Single.class);
			return objectMapper().readValue(inputStream, singles);
		} catch (IOException e) {
			throw new SingleParserException("Can`t parse json with singles", e);
		}
	}

	public Collection<Single> singles(String json) {
		try {
			CollectionType singles = TypeFactory.defaultInstance().constructCollectionType(List.class, Single.class);
			return objectMapper().readValue(json, singles);
		} catch (IOException e) {
			throw new SingleParserException("Can`t parse json with singles", e);
		}
	}

	private ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule();
		module.addDeserializer(Single.class, new SingleDeserializer());
		mapper.registerModule(module);

		return mapper;
	}

}
