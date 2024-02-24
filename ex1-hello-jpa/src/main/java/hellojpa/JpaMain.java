package hellojpa;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
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

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Team team2 = new Team();
            team2.setName("teamB");
            em.persist(team2);

            Member member1 = new Member();
            member1.setUsername("hello1");
            member1.changeTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("hello2");
            member2.changeTeam(team2);
            em.persist(member2);



            em.flush();
            em.clear();

            // fetch 조인 예시
            List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }

        emf.close();

    }
}
