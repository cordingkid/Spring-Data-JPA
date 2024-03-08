package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

// Ctrl + Shift + T 테스트 자동 만들어짐
/*
    최근 스프링 부트는 JUnit 5를 사용하기 때문에
    더이상 JUnit 4에서 제공하던 @RunWith를 쓸 필요가 없고 (쓰고 싶으면 쓸 수는 있지만),
    @ExtendWith를 사용해야 하지만,
    이미 스프링 부트가 제공하는 모든 테스트용 애노테이션(@SpringBootTest) 에 메타 애노테이션으로 적용되어 있기
    때문에 @ExtendWith(SpringExtension.class)를 생략할 수 있다.
 */
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional // spring 트랜잭션 사용 [트랜잭셔널 어노테이션이 테스트 케이스에 있으면 바로 롤백 해버린다.]
    @Rollback(value = false) // @Rollback(value = false) 해당 어노테이션 있으면 롤백 안하고 커밋 해버림
    public void testMember() throws Exception{
        // given
        Member member = new Member();
        member.setUsername("memberA");

        // when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // then
        // org.assertj.core.api.Assertions
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        // 질문 이건 같을까? 다를까? 나의 답: 현재 같은 트랜잭션에 있기 때문에 같다?
        // 정답!! O
        Assertions.assertThat(findMember).isEqualTo(member);
        System.out.println("findMember == member" + (findMember == member));
    }

}