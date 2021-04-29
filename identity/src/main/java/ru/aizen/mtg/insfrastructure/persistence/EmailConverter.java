package ru.aizen.mtg.insfrastructure.persistence;

import ru.aizen.mtg.domain.profile.Email;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EmailConverter implements AttributeConverter<Email, String> {

	@Override
	public String convertToDatabaseColumn(Email email) {
		return email == null ? null : email.getEmail();
	}

	@Override
	public Email convertToEntityAttribute(String email) {
		return Email.from(email);
	}

}
