package ru.aizen.mtg.order.domain.order;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;
import ru.aizen.mtg.order.domain.command.*;
import ru.aizen.mtg.order.domain.event.*;

import java.util.Collection;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@SuppressWarnings({"unused", "FieldCanBeLocal"})
@NoArgsConstructor
public class OrderAggregate {

	@AggregateIdentifier
	private String orderId;
	private long clientId;
	private String storeId;
	private OrderStatus status;
	private Collection<OrderItem> items;
	private double shippingCost;
	private String shippedTo;

	@CommandHandler
	public OrderAggregate(PlaceOrderCommand command) {
		Assert.notEmpty(command.getItems(), "Заказ пуст");

		apply(OrderEvent.place(command.getOrderId(), command.getClientId(), command.getStoreId(), command.getItems()));
	}

	@CommandHandler
	public void handle(ChangeShippingAddressCommand command) {
		Assert.hasText(command.getShippingAddress(), "Адрес доставки пуст");

		apply(OrderEvent.changeShippingAddress(command.getOrderId(), command.getShippingAddress()));
	}

	@CommandHandler
	public void handle(ChangeShippingCostCommand command) {
		apply(OrderEvent.changeShippingCost(command.getOrderId(), command.getShippingCost()));
	}

	@CommandHandler
	public void handle(RemoveOrderItemCommand command) {
		apply(OrderEvent.removeItem(command.getOrderId(), command.getItemId(), command.getItem()));
	}

	@CommandHandler
	public void handle(ConfirmOrderCommand command) {
		apply(OrderEvent.confirm(command.getOrderId()));
	}

	@CommandHandler
	public void handle(PayOrderCommand command) {
		apply(OrderEvent.pay(command.getOrderId(), command.getPaymentInfo()));
	}

	@CommandHandler
	public void handle(CancelOrderCommand command) {
		apply(OrderEvent.cancel(command.getOrderId()));
	}

	@CommandHandler
	public void handle(CompleteOrderCommand command) {
		apply(OrderEvent.complete(command.getOrderId()));
	}

	@EventSourcingHandler
	public void on(OrderPlacedEvent event) {
		this.orderId = event.getOrderId();
		this.clientId = event.getClientId();
		this.storeId = event.getStoreId();
		this.items = event.getItems();
		this.status = event.getStatus();
	}

	@EventSourcingHandler
	public void on(OrderConfirmedEvent event) {
		status = event.getStatus();
	}

	@EventSourcingHandler
	public void on(OrderCanceledEvent event) {
		status = event.getStatus();
	}

	@EventSourcingHandler
	public void on(OrderCompletedEvent event) {
		status = event.getStatus();
	}

}
