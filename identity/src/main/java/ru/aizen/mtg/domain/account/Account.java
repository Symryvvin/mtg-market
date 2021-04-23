package ru.aizen.mtg.domain.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.Assert;
import ru.aizen.mtg.domain.account.security.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "account")
public class Account {

	private static final int MAX_LOGIN_LENGTH = 50;

	@Id
	private long id;
	@Column(name = "login", nullable = false)
	private String login;
	@Column(name = "password", nullable = false)
	private String password;
	@Column(name = "is_blocked", nullable = false)
	private boolean blocked;
	@Column(name = "role", nullable = false)
	private Role role;

	private Account(String login, String password) {
		this.login = login;
		this.password = password;
		this.blocked = false;
		this.role = Role.CLIENT;
	}

	public static Account register(String login, String password) throws AccountException {
		Assert.hasLength(login, "Login is required");
		Assert.hasLength(password, "Password is required");
		if (login.length() > MAX_LOGIN_LENGTH) {
			throw new AccountException("The Login must be a string with a maximum length of " + MAX_LOGIN_LENGTH);
		}
		return new Account(login, password);
	}

	public void changePassword(String oldPassword, String newPassword) throws AccountException {
		Assert.hasLength(oldPassword, "Old password is required");
		Assert.hasLength(newPassword, "New password is required");
		if (oldPassword.equals(this.password)) {
			this.password = newPassword;
		} else {
			throw new AccountException("Old password does not match");
		}
	}

	public void block() {
		this.blocked = true;
	}

	public void unBlock() {
		this.blocked = false;
	}

	public void changeRole(Role role) {
		this.role = role;
	}

}
