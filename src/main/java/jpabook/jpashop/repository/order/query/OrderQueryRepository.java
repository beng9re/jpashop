package jpabook.jpashop.repository.order.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

	private final EntityManager em;

	public List<OrderQueryDto> findOrderQueryDtos(){
		List<OrderQueryDto> orders = findOrders();

		orders.forEach(o -> {
			List<OrderITemQueryDto> orderItems = findOrderItems(o.getOrderId());
			o.setOrderITemQueryDtoList(orderItems);
		});

		return orders;

	}

	private List<OrderITemQueryDto> findOrderItems(Long orderId) {
		return em.createQuery(
			"select new jpabook.jpashop.repository.order.query.OrderITemQueryDto"
				+ "(oi.order.id, i.name, oi.orderPrice, oi.count)"
				+ " from OrderItem oi"
				+ " join oi.item i"
				+ " where oi.order.id =:orderId",OrderITemQueryDto.class)
		.setParameter("orderId",orderId)
		.getResultList();

	}

	private List<OrderQueryDto> findOrders() {
		return em.createQuery(
				//(Long orderId, String name, LocalDateTime orderDAte, OrderStatus orderStatus,
			"select new jpabook.jpashop.repository.order.query.OrderQueryDto"
				+ " (o.id,m.name,o.orderDate,o.status,d.address) "
			+ "from Order o"
			+" join o.member m"
			+" join o.delivery d", OrderQueryDto.class)
			.getResultList();
	}

	public List<OrderQueryDto> findAllByDto_optimization() {
		List<OrderQueryDto> orders = findOrders();
		List<Long> orderIds = orders.stream().map(OrderQueryDto::getOrderId)
			.collect(Collectors.toList());
		//맵으로 1대1 초화
		Map<Long, List<OrderITemQueryDto>> orderItemsMap = getOrderIds(orderIds)
			.stream()
			.collect(Collectors.groupingBy(OrderITemQueryDto::getOrderId));

		orders.forEach(o -> o.setOrderITemQueryDtoList(orderItemsMap.get(o.getOrderId())));

		return orders;
	}

	private List<OrderITemQueryDto> getOrderIds(List<Long> orderIds) {
		return em.createQuery(
				"select new jpabook.jpashop.repository.order.query.OrderITemQueryDto"
					+ "(oi.order.id, i.name, oi.orderPrice, oi.count)"
					+ " from OrderItem oi"
					+ " join oi.item i"
					+ " where oi.order.id in:orderIds", OrderITemQueryDto.class)
			.setParameter("orderIds", orderIds)
			.getResultList();
	}

	public List<OrderFlatDto> findAllByDto_falt() {

		// Long orderId,
		// String name,
		// LocalDateTime orderDAte,
		// OrderStatus orderStatus,
		// 	Address address, List<OrderITemQueryDto> orderItems, String itemName, int orderPrice, int count
		return em.createQuery("select "
			+ " new jpabook.jpashop.repository.order.query.OrderFlatDto"
				+ "(o.id,"
				+ " m.name,"
				+ " o.orderDate,"
				+ " o.status, "
				+ " d.address,"
				+ " i.name,"
				+ " i.price,"
				+ " oi.count)"
			+ " from Order o"
			+ " join o.member m"
			+ " join o.delivery d"
			+ " join o.orderItems oi "
			+ "join oi.item i" , OrderFlatDto.class)
			.getResultList();
	}
}
