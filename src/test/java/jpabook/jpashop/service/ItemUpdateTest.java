package jpabook.jpashop.service;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jpabook.jpashop.domain.item.Book;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemUpdateTest {

	@Autowired
	EntityManager em;

	@Test
	public void updateTest() throws Exception {
		Book book = em.find(Book.class,1L);

		book.setName("aass");

		//변경감지
	}
}

