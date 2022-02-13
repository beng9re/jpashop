package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderRepository orderRepository;
	private final OrderQueryRepository orderQueryRepository;

	@GetMapping("/api/v1/orders")
	public List<Order> orderV1() {
		List<Order> all = orderRepository.findAllbyCriteria(new OrderSearch());
		for (Order order: all) {
			order.getMember().getName();
			order.getDelivery().getAddress();
			List<OrderItem> orderItems = order.getOrderItems();

			//강제 초기화
			orderItems.stream().forEach(o -> o.getItem().getName());
		}
		return all;
	}

	@GetMapping("/api/v2/orders")
	public List<OrderDto> orderV2() {
		List<Order> orders = orderRepository.findAllbyCriteria(new OrderSearch());

		return orders.stream().map(OrderDto::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/api/v3/orders")
	public List<OrderDto> orderV3() {
		return orderRepository.findAllWithItem().stream()
			.map(OrderDto::new)
			.collect(Collectors.toList());
	}


	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> orderV3_page(
		@RequestParam(value = "offset", defaultValue = "0") int offset,
		@RequestParam(value = "limit", defaultValue = "100") int limit
		) {

		return orderRepository.findAllWithMemberDelivery(offset,limit)
			.stream().map(OrderDto::new)
			.collect(Collectors.toList());
	}

	//JPA에서 DTO 직접조회
	@GetMapping("/api/v4/orders")
	public List<OrderQueryDto> orderV4() {
		return orderQueryRepository.findOrderQueryDtos();
	}

	@GetMapping("/api/v5/orders")
	public List<OrderQueryDto> orderV5() {
		return orderQueryRepository.findAllByDto_optimization();
	}

	@GetMapping("/api/v6/orders")
	public List<OrderQueryDto> orderV6() {

		List<OrderFlatDto> orderAllByDto_flat = orderQueryRepository.findAllByDto_falt();
		//TODO : 직접 DTO로 매칭하는 것을 만들어준다

		return new ArrayList<>();
	}
	@Getter
	static class OrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		// 내부에도 DTO
		private List<OrderItemDto> orderItems;

		public OrderDto(Order o) {
			orderId = o.getId();
			name = o.getMember().getName();
			orderDate = o.getOrderDate();
			orderStatus = o.getStatus();
			address = o.getDelivery().getAddress();
			//o.getOrderItems().stream().forEach(x -> x.getItem().getName());
			orderItems = o.getOrderItems().stream()
				.map(OrderItemDto::new)
				.collect(Collectors.toList());
		}
	}
//	내부에도 DTO로 만환을 해준다 API스펙이 깨지기 때문
	@Getter
	static class OrderItemDto {

		private String itemName; //상품명
		private int orderPrice; // 주문가격
		private int count; //주문수량

		public OrderItemDto(OrderItem orderItem) {
			itemName = orderItem.getItem().getName();
			orderPrice = orderItem.getOrderPrice();
			count = orderItem.getCount();
		}
	}

}
