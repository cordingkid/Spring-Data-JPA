package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 구현하지 않아도 동작한다?
    // 이게 쿼리 매소드 기능이다.
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 예시
    List<Member> findTop3HelloBy();

    // @Query(name = "Member.findByUsername") // 네임드 쿼리 사용법 주석 처리해도 잘 동작한다.
    List<Member> findByUsername(@Param("username") String username); // Param은 jpql을 명확히 작성 했을때 네임드 파람으로 넘거가야해서 적음

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("""
            select
                new study.datajpa.dto.MemberDto(m.id, m.username, t.name)
            from Member m
            join m.team t
            """)
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);
}
