package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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

    @PersistenceContext EntityManager em;

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

    @Test
    public void paging() throws Exception{
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        /**
         * Page, Pageable 사용
         */
        PageRequest pageRequest = PageRequest
                .of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // total 카운트 쿼리를 같이 날려준다
        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        /**
         * Dto로 변환
         * 이렇게 dto로 변환해서 반환 한다 (권장)
         *
         * Page안에 모든 것이 json으로 반환 된다.
         * 그리고 이 내부 컨텐트도 json으로 나가고
         * 그리고 외부로 response body로 쭉 반환해서 보내면
         * 깔끔하게 나오고 난 퇴근 하면된다!ㅋㅋㅋ
         */
        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        // 내부에 있는 실제 데이터 꺼낼 때
        // then
        List<Member> content = page.getContent();

        // totalcount 나옴
        long totalCount = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호 출력
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 출력
        assertThat(page.isFirst()).isTrue(); // 첫번째 페이지냐?
        assertThat(page.hasNext()).isTrue(); // 다음 페이지 있냐?
    }

    @Test
    public void bulkUpdate() throws Exception{
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultConut = memberRepository.bulkAgePlus(20);

        List<Member> result = memberRepository.findByUsername("member5");
        Member member = result.get(0); // 40살이 나온다.
        System.out.println("member = " + member);

        // then
        assertThat(resultConut).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception{
        // given
        // member1 -> teamA
        // member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        member1.changeTeam(teamA);
        member2.changeTeam(teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() throws Exception{
        // given
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2"); // 밑에 flush 될때 더티 체킹으로 인한 업데이트

        em.flush();
    }

    @Test
    public void lock() throws Exception{
        // given
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        // when
        List<Member> member1 = memberRepository.findLockByUsername("member1");
    }

}