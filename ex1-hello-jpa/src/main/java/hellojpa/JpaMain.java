package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // hello 유닛네임 persistence.xml 5줄 name
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        // EntityManagerFactory는 애플리케이션 로딩 시점에 딱 하나만 만들어 놔야한다.

        // DB 커넥션을 얻어서 쿼리를 날리고 종료되는 그런거 할때는 EntityManager가 꼭 만들어 줘야 한다.
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // JPA에서는 트랜잭션이 중요해서 트랜잭션을 시작해야한다.

        try {
            /*Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");*/

            /*Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");*/

            // 전체 회원 조회
            /*List<Member> result = em.createQuery("select m from Member as m", Member.class) // 엔티티 객체를 대상으로 검색
                    .setFirstResult(1) // 페이지 네이션 하는게 쉽ㄴ다
                    .setMaxResults(10)
                    .getResultList();*/

           /* for (Member member : result) {
                System.out.println("member.name = " + member.getName());
            }*/

            // 비영속 상태
            /*Member member = new Member();
            member.setId(101L);
            member.setName("HelloJPA");

            // 영속
            System.out.println("=== BEFORE ===");
            em.persist(member);
            // 이렇게 하면 영속성 컨텍스트에서 지운다.
            *//*em.detach(member);*//*
            System.out.println("=== AFTER ===");*/

            
            /*Member findMember = em.find(Member.class, 101L);
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());*/

            /*Member findMember1 = em.find(Member.class, 101L);
            Member findMember2 = em.find(Member.class, 101L);

            // 영속 엔티티의 동일성 보장
            System.out.println("result = " + (findMember1 == findMember2));  // true 값이 나온다.*/


            // 쓰기 지연 커밋시 쿼리가 나간다.
           /* Member member1 = new Member(150L, "A");
            Member member2 = new Member(160L, "B");
            em.persist(member1);
            em.persist(member2);*/

            // 변경 감지 Dirty Checking
            /*Member member = em.find(Member.class, 150L);
            member.setName("ZZZZZ");*/

            // 영속성 컨텍스트 플러시 하는 방법
            /*Member member = new Member(200L, "member200");
            em.persist(member);

            // 강제로 플러시 하기 쿼리가 먼저 나간다 플러시 한다고 1차 캐시가 지워지지 않음
            em.flush();*/


            // 영속상태
           /* Member member = em.find(Member.class, 150L);
            member.setName("AAAAA");*/

            // 준영속 상태 영속 상태에서 뻇기 때문에 update쿼리는 나가지 않는다.
            /*em.detach(member);*/

            // 영속성 컨텍스트를 통째로 지워버린다.
           /* em.clear();

            Member member2 = em.find(Member.class, 150L);*/

            // 그럼 궁금한거 이렇게하면 다르게 나올까?
            /*System.out.println(member == member2);*/ // false가 나옴 둘이 갖지 않다라고 나온다.

            /*Member member = new Member();
            member.setUsername("AAA");

            System.out.println("==============");
            em.persist(member);
            System.out.println("member.getId() = " + member.getId());
            System.out.println("==============");*/

            Member member1 = new Member();
            member1.setUsername("A");

            Member member2 = new Member();
            member1.setUsername("B");

            Member member3 = new Member();
            member1.setUsername("C");

            System.out.println("=================");

            // allocationSize = 50을 했을 경우
            // 처음 호출 한값 1       | 1
            // 두번쨰 호출 한값 51    | 2

            em.persist(member1); // 1, 51
            em.persist(member2); // memory에서 호출
            em.persist(member3); // memory에서 호출

            System.out.println("member1 = " + member1.getId());
            System.out.println("member2 = " + member2.getId());
            System.out.println("member3 = " + member3.getId());

            System.out.println("=================");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();

    }
}
