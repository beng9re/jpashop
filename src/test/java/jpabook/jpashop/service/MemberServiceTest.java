package jpabook.jpashop.service;



import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class MemberServiceTest {

	@Autowired MemberService memberService;
	@Autowired MemberRepository memberRepository;
	@Autowired EntityManager em;

	@Test
	@Rollback(value = false)
	public void 회원가입() throws Exception {
		//given
		Member member = new Member();
		member.setName("kim");

		//when
		Long saveId = memberService.join(member);

		//then 롤백을 하기때문에 인설트문이 안 생김
		em.flush();
		assertThat(member).isEqualTo(memberRepository.findOne(saveId));


	}

	@Test
	public void 중복_회원_예외() throws Exception {
		//given
		Member member = new Member();
		member.setName("Kim1");
		Member member2 = new Member();
		member2.setName("Kim1");
		//when
		memberService.join(member);

		//then
		assertThatIllegalStateException()
			.isThrownBy(()-> memberService.join(member2));



	}

}