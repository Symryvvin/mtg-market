package ru.aizen.mtg.store.domain.single;

import java.util.Arrays;

public enum Condition {

	M("Mint", "M"),
	NM("Near Mint", "NM"),
	SP("Slightly Played", "SP"),
	MP("Moderately Played", "MP"),
	HP("Heavy Played", "HP"),
	D("Damaged", "D"),
	UNDEFINED("Undefined", "U");

	private final String condition;
	private final String value;

	Condition(String condition, String value) {
		this.condition = condition;
		this.value = value;
	}

	public static Condition from(String value) {
		return Arrays.stream(Condition.values())
				.filter(c -> c.value.equalsIgnoreCase(value))
				.findFirst()
				.orElse(UNDEFINED);
	}

	public String getCondition() {
		return condition;
	}

	public String getValue() {
		return value;
	}

}
