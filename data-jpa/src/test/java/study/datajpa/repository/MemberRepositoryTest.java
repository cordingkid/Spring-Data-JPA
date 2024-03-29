package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired EntityManager em;

    @Autowired TeamRepository teamRepository;

    @Test
    public void 테스트_멤버() throws Exception{

        // memberRepository.getClass() = class jdk.proxy3.$Proxy126
        // 이 인터페이스를 보고 Spring Data JPA가 구현 클래스를 만들어서 꽂아 버린거다!
        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
        // given
        Member member = new Member("회원1");
        Member savedMember = memberRepository.save(member);
        // when
        Member findMember = memberRepository.findById(savedMember.getId()).get(); // 원래 이렇게 쓰면 안되는데 연습용이라

        // then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() throws Exception{
        // Spring Data Repository 검증

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get(); // 옵셔널 이렇게 쓰면 안되는데 테스트여서 썻다.

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() throws Exception{
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void findHelloBy() throws Exception{
        List<Member> top3HelloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testNamedQuery() throws Exception{
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void testQuery() throws Exception{
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(member1);
    }

    @Test
    public void findUsernameList() throws Exception{
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> result = memberRepository.findUsernameList();
        assertThat(result.size()).isEqualTo(2);
        System.out.println("result = " + result);
    }

    @Test
    public void findMemberDto() throws Exception{
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA", 10);
        member1.changeTeam(team);
        memberRepository.save(member1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
        assertThat(memberDto.size()).isEqualTo(1);
    }

    @Test
    public void findByNames() throws Exception{
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member byName : byNames) {
            System.out.println("byName = " + byName);
        }
    }

    @Test
    public void returnType() throws Exception{
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

//        em.flush();
//        em.clear();

        // 컬렉션 조회 할때 값이 없으면 null이 아니고 빈값을 반환한다.
        List<Member> aaa = memberRepository.findListByUsername("AAA"); 
        assertThat(aaa.get(0).getUsername()).isEqualTo("AAA");

        
        // 만약 단건 조회시 값이 단건이 아니고 여러건이 들어오면 Exception이 터진다.
        // IncorrectResultSizeDataAccsessException 인가 터짐
        
        // 단건 조회는 값이 없으면 null 갑을 반환한다.
        Member aaa1 = memberRepository.findMemberByUsername("AAA");
        assertThat(aaa1).isEqualTo(member1);

        // 그래서 Optional을 사용한다.
        Optional<Member> aaa2 = memberRepository.findOptionalMemberByUsername("AAA");
        assertThat(aaa2.get()).isEqualTo(member1);
    }
}