package ru.aizen.mtg.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsAccountByLoginIgnoreCase(String login);

	Optional<Account> findByLoginIgnoreCase(String login);

}
