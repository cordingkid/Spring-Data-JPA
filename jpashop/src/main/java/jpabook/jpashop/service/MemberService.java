package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // JPA는 트랜잭션 안에서 해결되어야 하기 떄문에 어노테이션 설정 | 스프링 트랜잭션 어노테이션 쓰는걸 추천 쓸게 많음
@RequiredArgsConstructor // final이 붙은 필드만 생성자 생성
public class MemberService {

    private final MemberRepository memberRepository; // 요즘은 이런식으로 주입한다. 생성자 인젝션

    /**
     * 회원 가입
     * @param member
     * @return
     */
    @Transactional // 여기에서 트랜잭션 거는게 우선권을 가지기때문에 위에 readOnly 무시
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        /*
            가끔씩 같은이름으로 여럿이서 동시에 가입 하는 경우가 있을수있음
            멑티쓰레드 환경이라 그래서 해당 매소드가 통과가 되는 경우가 있다
            그런 최악의 상황을 대비해서 데이터베이스에서 해당 멤버의 네임을 [유니크 제약 조건]을 걸어주는것이 좋다!
         */
        List<Member> findMembers = memberRepository.findByName(member.getName());
        // 사실은 맴버 수를 세서 그게 0보다 크면 문제가 있다 알리는게 최적화 하는법이라고함
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    // 조회에서 (readOnly = true) 옵션을 주면 JPA가 좀더 성능을 최적화 한다.
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
