package ru.aizen.mtg.domain.account;

import org.junit.jupiter.api.Test;
import ru.aizen.mtg.domain.account.security.Role;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

	@Test
	void register() throws AccountException {
		Account account = Account.register("login", "password");
		assertEquals("login", account.getLogin());
		assertEquals("password", account.getPassword());
		assertTrue(account.getRoles().contains(Role.CLIENT));
		assertFalse(account.isBlocked());
	}

	@Test
	void registerWithLongLogin() {
		assertThrows(AccountException.class,
				() -> Account.register("log----------------------------------------------in", "password"));
	}

	@Test
	void changePassword() throws AccountException {
		Account account = Account.register("login", "password");
		account.changePassword("password", "new_password");
		assertEquals("new_password", account.getPassword());
	}

	@Test
	void changePasswordWithNotMatchOldPassword() throws AccountException {
		Account account = Account.register("login", "password");
		assertThrows(AccountException.class, () ->
				account.changePassword("pass", "new_password"));


	}

	@Test
	void block() throws AccountException {
		Account account = Account.register("login", "password");
		account.block();
		assertTrue(account.isBlocked());
		account.unBlock();
		assertFalse(account.isBlocked());
	}

	@Test
	void addRole() throws AccountException {
		Account account = Account.register("login", "password");
		account.addRole(Role.MANAGER);
		assertTrue(account.getRoles().contains(Role.CLIENT) && account.getRoles().contains(Role.MANAGER));
	}
}