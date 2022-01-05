package jpabook.jpashop;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberRepositoryTest {


	@Autowired MemberRepository memberRepository;


	@Test
	@Transactional
	@Rollback(value = false)
	void testMember() {
		//given
		Member member = new Member();
		member.setUsername("meberA");
		Long saveId = memberRepository.save(member);

		//when
		Member findMember = memberRepository.find(saveId);

		//then
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(member);
		//같은 영속성 컨텍스트면 동일값 발생

		System.out.println("findMember == member = " + (findMember == member));
	}
}