package ru.aizen.mtg.order.application.resource;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aizen.mtg.order.application.resource.request.PlaceOrderDTO;
import ru.aizen.mtg.order.application.resource.response.Success;
import ru.aizen.mtg.order.application.resource.response.order.OrderPresentation;
import ru.aizen.mtg.order.domain.command.*;
import ru.aizen.mtg.order.domain.event.OrderEvent;
import ru.aizen.mtg.order.domain.order.Order;
import ru.aizen.mtg.order.domain.query.ClientOrderListQuery;
import ru.aizen.mtg.order.domain.query.OrderQuery;
import ru.aizen.mtg.order.domain.query.StoreOrderListQuery;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class OrderResource {

	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;
	private final EventStore eventStore;

	@Autowired
	public OrderResource(CommandGateway commandGateway, QueryGateway queryGateway, EventStore eventStore) {
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
		this.eventStore = eventStore;
	}

	@PostMapping
	public ResponseEntity<String> place(@RequestBody PlaceOrderDTO dto) {
		String orderId = commandGateway.sendAndWait(
				new PlaceOrderCommand(
						UUID.randomUUID().toString(),
						dto.getClientId(),
						dto.getStoreId(),
						dto.getItems())
		);
		return ResponseEntity.ok(orderId);
	}

	private void checkStatus(Order order) {
		if (order.isOnEndStatus()) {
			throw new IllegalStateException("Нельзя изменить отметенный или завершенный заказ");
		}
	}

	@PutMapping(path = "/{orderId}/change/address", consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Success> changeAddress(@PathVariable("orderId") String orderId, @RequestBody String address) {
		Order order = queryGateway.query(new OrderQuery(orderId), Order.class).join();
		checkStatus(order);
		commandGateway.sendAndWait(new ChangeShippingAddressCommand(orderId, address));
		return ResponseEntity.ok(Success.OK("Адрес доставки добавлен"));
	}

	@PutMapping(path = "/{orderId}/change/cost", consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Success> changeCost(@PathVariable("orderId") String orderId, @RequestBody String cost) {
		Order order = queryGateway.query(new OrderQuery(orderId), Order.class).join();
		checkStatus(order);
		commandGateway.sendAndWait(new ChangeShippingCostCommand(orderId, Double.parseDouble(cost)));
		return ResponseEntity.ok(Success.OK("Стоимость доставки изменена"));
	}

	@PutMapping(path = "/{orderId}/change/items", consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Success> changeItems(@PathVariable("orderId") String orderId,
	                                           @RequestBody String itemId) {
		Order order = queryGateway.query(new OrderQuery(orderId), Order.class).join();
		checkStatus(order);
		String itemName = order.itemName(itemId);
		commandGateway.sendAndWait(new RemoveOrderItemCommand(orderId, itemId, itemName));
		return ResponseEntity.ok(Success.OK("Позиция " + itemName + " была удалена из заказа"));
	}

	@PutMapping("/{orderId}/confirm")
	public ResponseEntity<Success> confirm(@PathVariable("orderId") String orderId) {
		Order order = queryGateway.query(new OrderQuery(orderId), Order.class).join();
		checkStatus(order);
		commandGateway.sendAndWait(new ConfirmOrderCommand(orderId));
		return ResponseEntity.ok(Success.OK("Заказ № " + order.orderNumber() + " подтвержден и ожидает оплаты"));
	}

	@PutMapping("/{orderId}/pay")
	public ResponseEntity<Success> pay(@PathVariable("orderId") String orderId, @RequestBody String paymentInfo) {
		Order order = queryGateway.query(new OrderQuery(orderId), Order.class).join();
		checkStatus(order);
		commandGateway.sendAndWait(new PayOrderCommand(orderId, paymentInfo));
		return ResponseEntity.ok(Success.OK("Заказ № " + order.orderNumber() + " оплачен"));
	}

	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Success> cancel(@PathVariable("orderId") String orderId) {
		Order order = queryGateway.query(new OrderQuery(orderId), Order.class).join();

		checkStatus(order);
		commandGateway.sendAndWait(new CancelOrderCommand(orderId));
		return ResponseEntity.ok(Success.OK("Заказ № " + order.orderNumber() + " отменен"));
	}

	@PutMapping("/{orderId}/complete")
	public ResponseEntity<Success> complete(@PathVariable("orderId") String orderId) {
		Order order = queryGateway.query(new OrderQuery(orderId), Order.class).join();
		checkStatus(order);
		commandGateway.sendAndWait(new CompleteOrderCommand(orderId));
		return ResponseEntity.ok(Success.OK("Заказ № " + order.orderNumber() + " завершен"));
	}

	@GetMapping("/{orderId}")
	public OrderPresentation view(@PathVariable("orderId") String orderId) {
		Collection<OrderEvent> events = eventStore.readEvents(orderId).asStream()
				.map(Message::getPayload)
				.filter(event -> event instanceof OrderEvent)
				.map(event -> (OrderEvent) event)
				.collect(Collectors.toList());

		Order order = queryGateway.query(new OrderQuery(orderId), Order.class).join();
		return OrderPresentation.from(order, events);
	}

	@GetMapping("/all/store/{storeId}")
	public Collection<OrderPresentation> storeOrders(@PathVariable("storeId") String storeId) {
		Collection<Order> orders = queryGateway.query(new StoreOrderListQuery(storeId),
				ResponseTypes.multipleInstancesOf(Order.class)).join();

		return orders.stream().map(
				order -> OrderPresentation.from(order, Collections.emptyList()))
				.collect(Collectors.toList());
	}

	@GetMapping("/all/client")
	public Collection<OrderPresentation> storeOrders(  @RequestHeader("X-UserId") Long clientId) {
		Collection<Order> orders = queryGateway.query(new ClientOrderListQuery(clientId),
				ResponseTypes.multipleInstancesOf(Order.class)).join();

		return orders.stream().map(
				order -> OrderPresentation.from(order, Collections.emptyList()))
				.collect(Collectors.toList());
	}

}
