package ru.aizen.mtg.search.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.aizen.mtg.search.domain.card.Card;
import ru.aizen.mtg.search.domain.card.CardRepository;
import ru.aizen.mtg.search.domain.card.Language;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SearchService {

	private final CardRepository cardRepository;

	private static final int MAX_SEARCH_RESULT_LIMIT = 15;
	private static final Pattern NOT_WORD_PATTERN = Pattern.compile("\\W", Pattern.UNICODE_CHARACTER_CLASS);

	private Collection<OracleCard> cardCollection;

	@Autowired
	public SearchService(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@PostConstruct
	private void postConstruct() {
		cardCollection = cardRepository.findAll().stream()
				.filter(card -> card.getLanguage() == Language.EN || card.getLanguage() == Language.RU)
				.map(OracleCard::new)
				.distinct()
				.collect(Collectors.toList());
	}

	public Collection<OracleCard> findByNameText(String findString) {
		Collection<OracleCard> result;
		if (NOT_WORD_PATTERN.matcher(findString).find()) {
			result = findByNameText(findString, cardCollection.stream());
		} else {
			result = findByWordInName(findString, cardCollection.stream());
			if (result.isEmpty()) {
				result = findByNameText(findString, cardCollection.stream());
			}
		}
		return result.stream()
				.limit(MAX_SEARCH_RESULT_LIMIT)
				.sorted(Comparator.comparingInt(c -> c.getName().length()))
				.collect(Collectors.toList());
	}

	private Collection<OracleCard> findByNameText(String findString, Stream<OracleCard> stream) {
		return stream.filter(card -> card.getName().toLowerCase().contains(findString.toLowerCase()))
				.collect(Collectors.toList());
	}

	private Collection<OracleCard> findByWordInName(String findString, Stream<OracleCard> stream) {
		Pattern pattern = Pattern.compile(
				"\\b" + findString,
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
		return stream.filter(card -> pattern.matcher(card.getName()).find())
				.collect(Collectors.toList());
	}

	public String oracleIdByPrintedName(String printedName) {
		Assert.hasLength(printedName, "Card printed name is empty");
		return cardRepository.findByPrintedName(printedName)
				.stream()
				.map(Card::getOracleId)
				.distinct()
				.findAny()
				.orElse(null);
	}

	public Collection<Card> printByCardOracleId(String oracleId) {
		Assert.hasLength(oracleId, "Card identifier is empty");
		return cardRepository.findByOracleId(UUID.fromString(oracleId).toString());
	}

	public Collection<String> languagesForSet(String oracleId, String setCode) {
		Assert.hasLength(oracleId, "Card identifier is empty");
		Assert.hasLength(setCode, "Card set code is empty");
		return cardRepository.findByOracleId(oracleId).stream()
				.filter(card -> card.getSetCode().equalsIgnoreCase(setCode))
				.map(card -> card.getLanguage().getCode())
				.collect(Collectors.toSet());
	}

	public Collection<String> setsForOracleId(String oracleId) {
		Assert.hasLength(oracleId, "Card identifier is empty");
		return cardRepository.findByOracleId(oracleId).stream()
				.map(Card::getSetCode)
				.collect(Collectors.toSet());
	}

}
