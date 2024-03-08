package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository // 스프링이 제공하는 기본타입 컴포넌트 대상 스캔되서 자동으로 스프링 빈에 등록됨
public class MemberRepository {

    // 스프링 부트가 @PersistenceContext 이 어노테이션이 있으면 주입 해줌
    @PersistenceContext
    private EntityManager em;

    /**
     * member.id를 반환하는 이유
     * 커맨드랑 쿼리를 분리하는 원칙
     * 저장을 하면 가급적이면 이거는 사이드 이팩트를 일으키는 커맨드성이기 떄문에
     * 리턴값을 거의 안만든다. 대신에 아이디 정도 있으면 다음에 다시 조회할 수 있으니까
     * 그래서 이렇게 아이디 정도만 반환함!
     * @param member
     * @return
     */
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    /**
     * member 조회
     * @param id
     * @return
     */
    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
