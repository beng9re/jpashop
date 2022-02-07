package jpabook.jpashop;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitDb {

	private final InitService initService;

	@PostConstruct
	public void init() {
		initService.dbInit1();
		initService.dbInit2();

	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {
		private final EntityManager entityManager;


		public void dbInit1(){
			Member member = new Member();
			member.setName("userA");
			member.setAddress(new Address("서울","1","11111"));
			entityManager.persist(member);

			Book book = new Book();
			book.setName("JPA1");
			book.setPrice(10000);
			book.setStockQuantity(100);

			Book book2 = new Book();
			book2.setName("JPA1");
			book2.setPrice(20000);
			book2.setStockQuantity(200);

			entityManager.persist(book);
			entityManager.persist(book2);

			OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 1);
			OrderItem orderItem1 = OrderItem.createOrderItem(book2, 20000, 2);

			Delivery delivery = createDelivery(member);
			Order order = Order.createOrder(member, delivery, orderItem, orderItem1);
			entityManager.persist(order);

		}

		public void dbInit2(){
			Member member = createMember("user2","인천","2222","3333");

			Book book = createBook("SPRING",2000,10000);
			Book book2 = createBook("SPRING2",2000,20000);

			entityManager.persist(book);
			entityManager.persist(book2);

			OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 1);
			OrderItem orderItem1 = OrderItem.createOrderItem(book2, 20000, 2);

			Delivery delivery = createDelivery(member);

			Order order = Order.createOrder(member, delivery, orderItem, orderItem1);
			entityManager.persist(order);

		}

		private Delivery createDelivery(Member member) {
			Delivery delivery =new Delivery();
			delivery.setAddress(member.getAddress());
			return delivery;
		}

		private Book createBook(String name,int price, int qty) {
			Book book = new Book();
			book.setName("JPA1");
			book.setPrice(10000);
			book.setStockQuantity(100);
			return book;
		}

		private Member createMember(String name, String city, String street,String zipcode) {
			Member member = new Member();
			member.setName(name);
			member.setAddress(new Address(city,street,zipcode));
			entityManager.persist(member);
			return member;
		}
	}

}




