package jpabook.jpashop.repository.order.query;

import java.time.LocalDateTime;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderFlatDto {


	private Long orderId;
	private String name;
	private LocalDateTime orderDAte;
	private OrderStatus orderStatus;
	private Address address;


	private String itemName;
	private int orderPrice;
	private int count;

	public OrderFlatDto(Long orderId, String name, LocalDateTime orderDAte, OrderStatus orderStatus,
		Address address, String itemName, int orderPrice, int count) {
		this.orderId = orderId;
		this.name = name;
		this.orderDAte = orderDAte;
		this.orderStatus = orderStatus;
		this.address = address;

		this.itemName = itemName;
		this.orderPrice = orderPrice;
		this.count = count;
	}
}
