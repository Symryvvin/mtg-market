package ru.aizen.mtg.application;

import lombok.Getter;
import lombok.ToString;
import ru.aizen.mtg.domain.account.Account;
import ru.aizen.mtg.domain.profile.Profile;

@Getter
@ToString
public class UserInfo {

	private final String login;
	private final String fullName;
	private final String email;
	private final String phone;
	private final String address;

	private UserInfo(String login, String fullName, String email, String phone, String address) {
		this.login = login;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public static UserInfo from(Account account) {
		Profile profile = account.getProfile();
		if (profile == null) {
			return new UserInfo(account.getLogin(), null, null, null, null);
		}
		return new UserInfo(
				account.getLogin(),
				profile.getFullName(),
				profile.getEmail() == null ? null : profile.getEmail().getEmail(),
				profile.getPhone() == null ? null : profile.getPhone().getFormattedPhone(),
				profile.getAddress() == null ? null : profile.getAddress().inline()
		);
	}

}
