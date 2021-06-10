package ru.aizen.mtg.order.domain.order;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@Accessors(fluent = true)
@Data
@Document(collection = "order")
public class Order {

	private String orderId;
	private String orderNumber;
	private long clientId;
	private String storeId;
	private OrderStatus status;
	private Collection<OrderItem> items;
	private double shippingCost;
	private String shippedTo;

	private Order(String orderId,
	             String orderNumber,
	             long clientId,
	             String storeId,
	             OrderStatus status,
	             Collection<OrderItem> items) {
		this.orderId = orderId;
		this.orderNumber = orderNumber;
		this.clientId = clientId;
		this.storeId = storeId;
		this.status = status;
		this.items = items;
	}

	public static Order create(String orderId,
	                           String orderNumber,
	                           long clientId,
	                           String storeId,
	                           OrderStatus status,
	                           Collection<OrderItem> items) {
		return new Order(orderId, orderNumber, clientId, storeId, status, items);
	}

}
