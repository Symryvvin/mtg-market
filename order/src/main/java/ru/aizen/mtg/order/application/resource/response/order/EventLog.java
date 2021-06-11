package ru.aizen.mtg.order.application.resource.response.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.aizen.mtg.order.domain.event.OrderEvent;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EventLog {

	private final LocalDateTime eventTime;
	private final String message;

	public static EventLog from(OrderEvent event) {
		String message;
		if (event.getStatus() != null) {
			message = event.getStatus().getMessage();
			if (event.getMessage() != null) {
				message = message + ". " + event.getMessage();
			}
		} else {
			message = event.getMessage();
		}
		return new EventLog(event.getEventTime(), message);
	}

}
