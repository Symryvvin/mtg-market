package ru.aizen.mtg.search.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.aizen.mtg.search.domain.card.Card;
import ru.aizen.mtg.search.domain.card.CardRepository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SearchService {

	private final CardRepository cardRepository;

	private static final int MAX_SEARCH_RESULT_LIMIT = 15;
	private static final Pattern NOT_WORD_PATTERN = Pattern.compile("\\W", Pattern.UNICODE_CHARACTER_CLASS);

	private Collection<String> cardNameCollection;

	@Autowired
	public SearchService(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@PostConstruct
	private void postConstruct() {
		cardNameCollection = new ArrayList<>();
		cardRepository.findAll()
				.forEach(card -> cardNameCollection.add(card.getPrintedName()));
		cardNameCollection = cardNameCollection.stream().distinct().collect(Collectors.toList());
	}

	public Collection<String> findByPartOfName(String findString) {
		Collection<String> result;
		if (NOT_WORD_PATTERN.matcher(findString).find()) {
			result = findByPartOfName(findString, cardNameCollection.stream());
		} else {
			result = findByStartOfWord(findString, cardNameCollection.stream());
			if (result.isEmpty()) {
				result = findByPartOfName(findString, cardNameCollection.stream());
			}
		}
		return result.stream()
				.limit(MAX_SEARCH_RESULT_LIMIT)
				.sorted(Comparator.comparingInt(String::length))
				.collect(Collectors.toList());
	}

	private Collection<String> findByPartOfName(String findString, Stream<String> nameStream) {
		return nameStream.filter(cn -> cn.toLowerCase().contains(findString.toLowerCase()))
				.collect(Collectors.toList());
	}

	private Collection<String> findByStartOfWord(String findString, Stream<String> nameStream) {
		Pattern pattern = Pattern.compile(
				"\\b" + findString,
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
		return nameStream.filter(cn -> pattern.matcher(cn).find())
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
