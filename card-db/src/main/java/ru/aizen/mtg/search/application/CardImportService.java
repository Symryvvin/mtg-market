package ru.aizen.mtg.search.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.aizen.mtg.search.domain.card.Card;
import ru.aizen.mtg.search.domain.card.CardRepository;
import ru.aizen.mtg.search.domain.card.Language;
import ru.aizen.mtg.search.domain.importer.CardDownloader;
import ru.aizen.mtg.search.domain.importer.CardImporterException;
import ru.aizen.mtg.search.domain.parser.CardParser;
import ru.aizen.mtg.search.domain.parser.CardParserException;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CardImportService {

	private static final Logger logger = LoggerFactory.getLogger(CardImportService.class);

	private final String cardDataLocal;
	private final CardDownloader cardDownloader;
	private final CardParser cardParser;
	private final CardRepository cardRepository;

	@Autowired
	public CardImportService(
			@Value("${card.data.local}") String cardDataLocal,
			CardDownloader cardDownloader,
			CardParser cardParser, CardRepository cardRepository) {
		this.cardDataLocal = cardDataLocal;
		this.cardDownloader = cardDownloader;
		this.cardParser = cardParser;
		this.cardRepository = cardRepository;
	}

	@PostConstruct
	private void postConstruct() {
		importCards();
	}

	@Scheduled(cron = "${card.import.time}")
	public void importCards() {
		try {
			logger.info("Start import cards from {}", cardDataLocal);
			Path cardDataPath = cardDownloader.downloadDataTo(Paths.get(cardDataLocal));
			Collection<Card> cardsToImport = cardParser.parseCardFrom(cardDataPath);
			cardRepository.saveAll(cardsToImport);
		} catch (CardParserException | CardImporterException e) {
			logger.warn("Import cards error", e);
		}
	}

	@Deprecated
	private Collection<Card> filterCollection(Collection<Card> cards) {
		return cards.stream()
				.filter(card -> card.getLanguage() == Language.EN || card.getLanguage() == Language.RU)
				.collect(Collectors.toList());
	}

}
