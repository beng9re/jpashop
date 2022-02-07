package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
/**
 *
 *  XToOne
	ORDER -> MEMBER
	ORDER -> Delivery
*/
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;


	//양방한 문제
	@GetMapping("/api/vi/simple-orders")
	public List<Order> ordersV1() {
		List<Order> all = orderRepository.findAllbyCriteria(new OrderSearch());

		return all;
	}


	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> orderV2() {
		List<Order> orders = orderRepository.findAllbyCriteria(new OrderSearch());
		List<SimpleOrderDto> result = orders.stream()
			.map(SimpleOrderDto::new)
			.collect(Collectors.toList());

		return result;

	}

	// 패치조인
	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDto> orderV3() {
		return orderRepository.findAllWithMemberDelivery().stream()
			.map(SimpleOrderDto::new)
			.collect(Collectors.toList());

	}

	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> orderV4() {
		return orderSimpleQueryRepository.findOrderDtos();
	}



	@Data
	static class SimpleOrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;

		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName(); //Lazy 최고하
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
		}

	}
}
