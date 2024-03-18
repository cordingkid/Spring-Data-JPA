package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 컴포넌트 스캔대상이 되서 자동으로 스프링 빈으로 관리됨
@RequiredArgsConstructor
public class MemberRepositoryOld {

    // 스프링이 엔티티매니저를 만들어서 주입해줌 @PersistenceContext = @Autowired <= 스프링 데이터 JPA가 이걸 지원해줌
    // 생성자로 인잭션 한거다.
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
