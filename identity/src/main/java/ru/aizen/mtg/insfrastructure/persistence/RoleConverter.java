package ru.aizen.mtg.insfrastructure.persistence;

import ru.aizen.mtg.domain.account.security.Role;

import javax.persistence.AttributeConverter;

public class RoleConverter implements AttributeConverter<Role, String> {

	@Override
	public String convertToDatabaseColumn(Role role) {
		return role.name();
	}

	@Override
	public Role convertToEntityAttribute(String role) {
		return Role.find(role);
	}
}
