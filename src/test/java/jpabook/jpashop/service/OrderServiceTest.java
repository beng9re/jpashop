package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.apache.tomcat.jni.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class OrderServiceTest {

	@Autowired
	EntityManager em;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	@Test
	public void 상품주문 () throws Exception{
		//given
		Member member = createMember();

		Book book = createBook("ㄱㄱ", 1000,10);
		//when
		int orderCount = 2;

		Long order = orderService.order(member.getId(), book.getId(), orderCount);

		Order getOrder = orderRepository.findOne(order);

		assertThat(OrderStatus.ORDER).isEqualTo(getOrder.getStatus())
			.withFailMessage("상품주문시 상태는 ORDER이다");

		assertThat(1).isEqualTo(getOrder.getOrderItems().size())
			.withFailMessage("주문 상품종류수가 정확해야함");

		assertThat(1000 * orderCount).isEqualTo(getOrder.getTotalPrice())
			.withFailMessage("주문 수량만큼 재고가 줄어야한다");

		assertThat(8).isEqualTo(book.getStockQuantity())
			.withFailMessage("주문 수량만큼 재고가 줄어야함");

	}

	@Test
	public void 주문취소 () throws Exception{
		//gevien
		Member member = createMember();
		Book boo = createBook("시골 JP", 1000, 10);

		int orderCount = 2;
		Long order = orderService.order(member.getId(), boo.getId(), orderCount);

		orderService.cancelOrder(order);

		Order getOrder = orderRepository.findOne(order);

		assertThat(OrderStatus.CANCEL).isEqualTo(getOrder.getStatus())
			.withFailMessage("주문 취소시 상태는 Cacel 이다");

		assertThat(10).isEqualTo(boo.getStockQuantity())
			.withFailMessage("주문이 취소된 상품은 그만큼 재고 증가 해야함 ");



	}

	@Test
	public void 상품주문_재고수량초과 () throws Exception{
		Member member = createMember();
		Item book = createBook("ㄱㄱ", 1000,10);

		int orderCount = 11;


		assertThatExceptionOfType(NotEnoughStockException.class)
			.isThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount));

	}

	private Book createBook(String name, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		em.persist(book);
		return book;
	}

	private Member createMember() {
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("인천","a","a"));

		em.persist(member);
		return member;
	}


}