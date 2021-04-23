package ru.aizen.mtg.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsAccountByLogin(String login);

	Optional<Account> findByLogin(String login);

}
