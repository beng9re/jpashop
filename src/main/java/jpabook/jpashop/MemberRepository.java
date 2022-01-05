package jpabook.jpashop;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

	//엔티티 메니저 생성
	@PersistenceContext
	private EntityManager em;

	public Long save(Member member) {
		em.persist(member);
		return member.getId(); //CQS member 엔티티를 반환하면 사이드 이펙트 발생 여지 있음
	}

	public Member find(Long id) {
		return em.find(Member.class,id);
	}

}
