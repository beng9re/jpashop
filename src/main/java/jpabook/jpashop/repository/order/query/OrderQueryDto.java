package jpabook.jpashop.repository.order.query;

import java.time.LocalDateTime;
import java.util.List;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderQueryDto {

	private Long orderId;
	private String name;
	private LocalDateTime orderDAte;
	private OrderStatus orderStatus;
	private Address address;
	private List<OrderITemQueryDto> orderITemQueryDtoList;

	public OrderQueryDto(Long orderId, String name, LocalDateTime orderDAte, OrderStatus orderStatus,
		Address address) {
		this.orderId = orderId;
		this.name = name;
		this.orderDAte = orderDAte;
		this.orderStatus = orderStatus;
		this.address = address;
	}
}
