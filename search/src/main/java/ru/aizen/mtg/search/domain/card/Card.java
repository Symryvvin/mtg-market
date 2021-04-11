package ru.aizen.mtg.search.domain.card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "card")
public class Card {

	@Id
	private String id;
	@Column(name = "name")
	private String oracleName;
	@Column(name = "printed_name")
	private String printedName;
	@Column(name = "set_code")
	private String setCode;
	@Column(name = "lang")
	private Language language;

	private Card(String id, String oracleName, String printedName, String setCode, Language language) {
		this.id = id;
		this.oracleName = oracleName;
		this.printedName = printedName;
		this.setCode = setCode;
		this.language = language;
	}

	public static Card from(String id, String oracleName, String printedName, String printCode, String languageCode)
			throws NotSupportedLanguageException {
		Language language = Language.fromCode(languageCode);
		return new Card(id, oracleName, printedName, printCode, language);
	}

}