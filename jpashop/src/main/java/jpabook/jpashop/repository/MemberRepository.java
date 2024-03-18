package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JpaRepository<T, ID>
 * T => 타입 엔티티
 * ID => PK
 *
 * 사람이 생각할 수 있는 공통 매서드가 거의 다 만들어져있다.
 *
 * 그럼 구현체를 스프링 빈이 인젝션 해줘야 하는거 아닌가?
 * 구현체를 스프링 데이터 JPA가 다 알아서 만들어 준다.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * select m from Member m where m.name = ?
     * 구현 안하고 이런식으로 바로 만들어준다.
     * 근데 findBy[네임] 이런 규칙이 있다.
     */
    List<Member> findByName(String name);
}
