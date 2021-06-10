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
	private String orderNumber;
	private long clientId;
	private String storeId;
	private OrderStatus status;
	private Collection<OrderItem> items;
	private double shippingCost;
	private String shippedTo;

	@CommandHandler
	public OrderAggregate(PlaceOrderCommand command) {
		Assert.notEmpty(command.getItems(), "Заказ пуст");

		String orderNumber = command.getOrderId().substring(0, 4) + "-" +
				String.format("%05d", command.getClientId()) + "-" +
				command.getStoreId().substring(0, 4);

		apply(new OrderPlacedEvent(
				command.getOrderId(),
				orderNumber.toUpperCase(),
				command.getClientId(),
				command.getStoreId(),
				command.getItems())
		);
	}

	@CommandHandler
	public void handle(ConfirmOrderCommand command) {
		apply(new OrderConfirmedEvent(command.getOrderId()));
	}

	@CommandHandler
	public void handle(PayOrderCommand command) {
		apply(new OrderPaidEvent(command.getOrderId(), command.getPaymentInfo()));
	}

	@CommandHandler
	public void handle(CancelOrderCommand command) {
		apply(new OrderCanceledEvent(command.getOrderId()));
	}

	@CommandHandler
	public void handle(CompleteOrderCommand command) {
		apply(new OrderCompletedEvent(command.getOrderId()));
	}

	@EventSourcingHandler
	public void on(OrderPlacedEvent event) {
		this.orderId = event.getOrderId();
		this.clientId = event.getClientId();
		this.storeId = event.getStoreId();
		this.items = event.getItems();
		this.status = event.getStatus();
		this.orderNumber = event.getOrderNumber();
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
