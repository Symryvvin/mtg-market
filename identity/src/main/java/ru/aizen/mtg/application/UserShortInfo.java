package ru.aizen.mtg.application;

import lombok.Getter;
import lombok.ToString;
import ru.aizen.mtg.domain.account.Account;
import ru.aizen.mtg.domain.profile.Profile;

@Getter
@ToString
public class UserShortInfo {

	private final long id;
	private final String login;
	private final String settlement;

	private UserShortInfo(long id, String login, String settlement) {
		this.id = id;
		this.login = login;
		this.settlement = settlement;
	}

	public static UserShortInfo from(Account account) {
		Profile profile = account.getProfile();
		if (profile == null) {
			return new UserShortInfo(account.getId(), account.getLogin(), null);
		}
		return new UserShortInfo(account.getId(),
				account.getLogin(),
				profile.getAddress() == null ? null : profile.getAddress().getSettlement()
		);
	}

}
