package ru.aizen.mtg.order.domain.order;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collection;

@Accessors(fluent = true)
@Data
@Document(collection = "order")
public class Order {

	private String id;
	private String orderId;
	private String orderNumber;
	private long clientId;
	private long traderId;
	private OrderStatus status;
	private LocalDateTime creationDate;
	private LocalDateTime updatedTime;

	private Collection<OrderItem> items;
	private double shippingCost;
	private String shippedTo;

	private Order(String orderId,
	              String orderNumber,
	              long clientId,
	              long traderId,
	              OrderStatus status,
	              LocalDateTime updatedTime,
	              Collection<OrderItem> items) {
		this.orderId = orderId;
		this.orderNumber = orderNumber;
		this.clientId = clientId;
		this.traderId = traderId;
		this.status = status;
		this.items = items;
		this.creationDate = updatedTime;
		this.updatedTime = updatedTime;
	}

	public static Order create(String orderId,
	                           String orderNumber,
	                           long clientId,
	                           long traderId,
	                           OrderStatus status,
	                           LocalDateTime updatedTime,
	                           Collection<OrderItem> items) {
		return new Order(orderId, orderNumber, clientId, traderId, status, updatedTime, items);
	}

	public boolean isOnEndStatus() {
		return status.equals(OrderStatus.CANCELED) || status.equals(OrderStatus.COMPLETED);
	}

	public void removeItem(String itemId) {
		items.removeIf(item -> item.getSingleId().equalsIgnoreCase(itemId));
	}

	public String itemName(String itemId) {
		return items.stream()
				.filter(item -> item.getSingleId().equalsIgnoreCase(itemId))
				.map(OrderItem::getName)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Позиция с ID + [" + itemId + "] не найдена в заказе"));
	}

}
