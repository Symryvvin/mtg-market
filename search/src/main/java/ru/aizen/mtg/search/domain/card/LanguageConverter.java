package ru.aizen.mtg.search.domain.card;

import javax.persistence.AttributeConverter;

public class LanguageConverter implements AttributeConverter<Language, String> {

	@Override
	public String convertToDatabaseColumn(Language language) {
		return language.getCode();
	}

	@Override
	public Language convertToEntityAttribute(String code) {
		return Language.fromCode(code);
	}
}
