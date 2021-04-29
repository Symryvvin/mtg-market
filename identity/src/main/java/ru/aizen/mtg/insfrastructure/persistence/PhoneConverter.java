package ru.aizen.mtg.insfrastructure.persistence;

import ru.aizen.mtg.domain.profile.Phone;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PhoneConverter implements AttributeConverter<Phone, Long> {

	@Override
	public Long convertToDatabaseColumn(Phone phone) {
		return phone == null ? null : phone.getPhoneNumber();
	}

	@Override
	public Phone convertToEntityAttribute(Long number) {
		return Phone.from(number);
	}

}
