package ru.aizen.mtg.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aizen.mtg.domain.account.Account;
import ru.aizen.mtg.domain.account.AccountException;
import ru.aizen.mtg.domain.account.AccountRepository;
import ru.aizen.mtg.domain.account.security.PasswordSecure;
import ru.aizen.mtg.domain.account.security.PasswordSecureException;
import ru.aizen.mtg.domain.jwt.TokenService;
import ru.aizen.mtg.domain.profile.Email;
import ru.aizen.mtg.domain.profile.Phone;
import ru.aizen.mtg.domain.profile.Profile;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class IdentityService {

	private final AccountRepository accountRepository;
	private final PasswordSecure passwordSecure;
	private final TokenService tokenService;

	@Autowired
	public IdentityService(AccountRepository accountRepository,
	                       PasswordSecure passwordSecure,
	                       TokenService tokenService) {
		this.accountRepository = accountRepository;
		this.passwordSecure = passwordSecure;
		this.tokenService = tokenService;
	}

	@Transactional
	public void create(String login, String password) throws IdentityServiceException {
		try {
			if (accountRepository.existsAccountByLogin(login)) {
				throw new IdentityServiceException("User with login '" + login + "' already exists");
			} else {
				Account account = Account.register(login, passwordSecure.encrypt(password));
				accountRepository.save(account);
			}
		} catch (PasswordSecureException | AccountException e) {
			throw new IdentityServiceException(e);
		}
	}

	public String authenticate(String username, String password) throws IdentityServiceException {
		try {
			Optional<Account> accountOptional = accountRepository.findByLogin(username);

			if (accountOptional.isPresent()) {
				Account account = accountOptional.get();

				if (passwordSecure.validate(password, account.getPassword())) {
					return tokenService.generate(account);
				} else {
					throw new IdentityServiceException("Username or password is not valid");
				}
			} else {
				throw new IdentityServiceException("User '" + username + "' not found");
			}
		} catch (PasswordSecureException e) {
			throw new IdentityServiceException(e);
		}
	}

	public void changePassword(String username, String oldPassword, String newPassword) throws IdentityServiceException {
		try {
			Optional<Account> accountOptional = accountRepository.findByLogin(username);

			if (accountOptional.isPresent()) {
				accountOptional.get()
						.changePassword(passwordSecure.encrypt(oldPassword), passwordSecure.encrypt(newPassword));
			} else {
				throw new IdentityServiceException("User with name " + username + " not found");
			}
		} catch (PasswordSecureException | AccountException e) {
			throw new IdentityServiceException(e);
		}
	}

	public UserInfo userInfo(String username) throws IdentityServiceException {
		Optional<Account> accountOptional = accountRepository.findByLogin(username);

		if (accountOptional.isPresent()) {
			return UserInfo.from(accountOptional.get());
		} else {
			throw new IdentityServiceException("User with name " + username + " not found");
		}
	}

	public Collection<UserShortInfo> users() {
		return accountRepository.findAll().stream()
				.map(UserShortInfo::from)
				.collect(Collectors.toList());
	}

	public void updateProfile(String username, String fullName, String email, String phone) throws IdentityServiceException {
		Optional<Account> accountOptional = accountRepository.findByLogin(username);

		if (accountOptional.isPresent()) {
			Account account = accountOptional.get();
			account.updateProfile(fullName, email, phone);
			accountRepository.save(account);
		} else {
			throw new IdentityServiceException("User with name " + username + " not found");
		}
	}

	public void updateProfileAddress(String username, String settlement, String street, String building, String apartment, Integer postIndex)
			throws IdentityServiceException {
		Optional<Account> accountOptional = accountRepository.findByLogin(username);

		if (accountOptional.isPresent()) {
			Account account = accountOptional.get();
			account.updateAddress(settlement, street, building, apartment, postIndex);
			accountRepository.save(account);
		} else {
			throw new IdentityServiceException("User with name '" + username + "' not found");
		}
	}

}
