package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 구현하지 않아도 동작한다?
    // 이게 쿼리 매소드 기능이다.
    List<Member> findByUsername(String username);
}
