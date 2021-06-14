package ru.aizen.mtg.search.domain.card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "card")
public class Card {

	@Id
	private String id;
	@Column(name = "oracle_id")
	private String oracleId;
	@Column(name = "name")
	private String oracleName;
	@Column(name = "printed_name")
	private String printedName;
	@Column(name = "set_code")
	private String setCode;
	@Column(name = "lang")
	@Convert(converter = LanguageConverter.class)
	private Language language;

	private Card(String id, String oracleId, String oracleName, String printedName, String setCode, Language language) {
		this.id = id;
		this.oracleId = oracleId;
		this.oracleName = oracleName;
		this.printedName = printedName;
		this.setCode = setCode;
		this.language = language;
	}

	public static Card from(String id, String oracleId, String oracleName, String printedName, String printCode, String languageCode) {
		Language language = Language.fromCode(languageCode);
		return new Card(id, oracleId, oracleName, printedName, printCode, language);
	}

}