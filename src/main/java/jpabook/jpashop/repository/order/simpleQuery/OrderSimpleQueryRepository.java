package jpabook.jpashop.repository.order.simpleQuery;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

	private final EntityManager em;

	// 쿼리에 API 스펙에 의존한다 => 성능최적화시는 이것이 좋음
	public List<OrderSimpleQueryDto> findOrderDtos() {
		return em.createQuery("select new jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryDto("
					+ "o.id"
					+ ",o.member.name"
					+ ",o.orderDate"
					+ ",o.status"
					+ ",o.delivery.address) "
					+ "from Order o"
					+ " join o.member m"
					+ " join o.delivery d"
				, OrderSimpleQueryDto.class)
			.getResultList();

	}
	// JPA 쿼리 방식 선택 가이드

}
