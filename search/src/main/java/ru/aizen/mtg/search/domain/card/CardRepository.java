package ru.aizen.mtg.search.domain.card;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CardRepository extends CrudRepository<Card, UUID> {

}
