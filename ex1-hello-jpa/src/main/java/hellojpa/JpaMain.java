package hellojpa;

import jakarta.persistence.*;

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

            // 조인 전략
            Member member = new Member();
            member.setCreatedBy("kim");
            member.setCreatedDate(LocalDateTime.now());

            em.persist(member);

            em.flush();
            em.clear();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();

    }
}
