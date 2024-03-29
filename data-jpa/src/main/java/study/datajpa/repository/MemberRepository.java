package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    List<Member> findListByUsername(String username); // 컬렉션

    Member findMemberByUsername(String username); // 단던

    Optional<Member> findOptionalMemberByUsername(String username); // 단건 Optional

    /**
     * 페이징
     * import org.springframework.data.domain.Page;
     * import org.springframework.data.domain.Pageable;
     *
     * 이런식으로 카운트 쿼리를 분리해서 성능을 최적화 할 수 있다.
     */
    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * Slice 페이징
     */
//    Slice<Member> findByAge(int age, Pageable pageable);

    /**
     * bulk연산
     * @Modifying 이 있어야
     * executeUpdate이걸 호출 한다. 이게 없으면 singleResult 이런걸 호출한다.
     */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
