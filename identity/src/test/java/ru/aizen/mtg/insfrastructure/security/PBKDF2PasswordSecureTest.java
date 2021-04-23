package ru.aizen.mtg.insfrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.aizen.mtg.domain.account.security.PasswordSecure;
import ru.aizen.mtg.domain.account.security.PasswordSecureException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PBKDF2PasswordSecureTest {

	@Autowired
	private PasswordSecure passwordSecure;

	@Test
	void encryptValidate() throws PasswordSecureException {
		String password = "password";
		String encryptedPassword = passwordSecure.encrypt(password);
		assertTrue(passwordSecure.validate(password, encryptedPassword));
	}

}