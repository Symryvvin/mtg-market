package ru.aizen.mtg.order.application.rest.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AddSingleToCartDTO {

	private String storeId;
	private String singleId;

}
