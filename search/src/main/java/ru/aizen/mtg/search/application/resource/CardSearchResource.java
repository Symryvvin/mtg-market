package ru.aizen.mtg.search.application.resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.aizen.mtg.search.application.OracleCard;
import ru.aizen.mtg.search.application.SearchService;
import ru.aizen.mtg.search.domain.card.Card;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("api/v1")
@CrossOrigin
public class CardSearchResource {

	private static final int MINIMUM_NUMBER_OF_CHARS = 3;

	private final SearchService searchService;

	public CardSearchResource(SearchService searchService) {
		this.searchService = searchService;
	}

	@GetMapping(path = "auto", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<OracleCard> autocomplete(@RequestParam("search") String searchStr) {
		if (checkMinNumberOfCharacters(searchStr)) {
			return searchService.findByNameText(searchStr);
		}
		return Collections.emptyList();
	}

	private boolean checkMinNumberOfCharacters(String searchStr) {
		return searchStr.length() >= MINIMUM_NUMBER_OF_CHARS;
	}

	@GetMapping(path = "details", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Card> detailByOracleId(@RequestParam("oracle_id") String oracleId) {
		return searchService.printByCardOracleId(oracleId);
	}

	@GetMapping(path = "find/oracle_id", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findOracleIdBy(@RequestParam("printed_name") String printedName) {
		return searchService.oracleIdByPrintedName(printedName);
	}

	@GetMapping(path = "details/set", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<String> allSetsByOracleId(@RequestParam("oracle_id") String oracleId) {
		return searchService.setsForOracleId(oracleId);
	}

	@GetMapping(path = "details/lang", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<String> allLangForOracleIdBySet(@RequestParam("oracle_id") String oracleId, @RequestParam("set") String setCode) {
		return searchService.languagesForSet(oracleId, setCode);
	}

	@GetMapping(path = "random", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Card> random(@RequestParam("size") int size, @RequestParam("lang") String languageCode) {
		return searchService.randomCollection(size, languageCode);
	}

}
