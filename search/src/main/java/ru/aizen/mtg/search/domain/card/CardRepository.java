package ru.aizen.mtg.search.domain.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CardRepository extends JpaRepository<Card, String> {

	Collection<Card> findByOracleId(String oracleId);

	Collection<Card> findByPrintedName(String printedName);

}
