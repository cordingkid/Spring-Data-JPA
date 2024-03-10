package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 부트를 띄운 상태로 테스트 하기위한 어노테이션 이거 없으면 오토와이어드가 다 실패함
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager entityManager;


    @Test
//    @Rollback(value = false)
    public void 회원가입() throws Exception{
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long savedId = memberService.join(member);

        // then
        entityManager.flush(); // 쿼리를 보고싶을때 어차피 롤백 해줌
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
//        memberService.join(member2); // 여기서 예외 터져야함 이름이 같기 때문에
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));


        // then
        assertEquals("이미 존재하는 회원입니다.", e.getMessage());
//        fail("예외가 발생해야 한다."); // 위에서 예외가 터져야 하기때문에 이게 찍히면 안된다.
    }
}