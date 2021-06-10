package ru.aizen.mtg.order.application.resource;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.aizen.mtg.order.application.resource.request.PlaceOrderDTO;
import ru.aizen.mtg.order.application.resource.response.OrderPresentation;
import ru.aizen.mtg.order.domain.command.PlaceOrderCommand;
import ru.aizen.mtg.order.domain.order.Order;
import ru.aizen.mtg.order.domain.query.OrderQuery;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class OrderResource {

	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;

	@Autowired
	public OrderResource(CommandGateway commandGateway, QueryGateway queryGateway) {
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
	}

	@PostMapping
	public void registerBook(@RequestBody PlaceOrderDTO dto) {
		commandGateway.sendAndWait(
				new PlaceOrderCommand(
						UUID.randomUUID().toString(),
						dto.getClientId(),
						dto.getStoreId(),
						dto.getItems())
		);
	}

	@GetMapping("/{orderId}")
	public OrderPresentation view(@PathVariable("orderId") String orderId) {
		return OrderPresentation.from(queryGateway.query(new OrderQuery(orderId), Order.class).join());
	}

}
