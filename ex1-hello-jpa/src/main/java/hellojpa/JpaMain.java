package hellojpa;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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

            // JPQL
            // flush -> commit, query
//            List<Member> result = em.createQuery(
//                    "select m from Member m where m.username like '%kim%'", // 여기 Member는 테이블이 아니고 엔티티를 가르킨다.
//                    Member.class
//            ).getResultList();
            
            // Criteria 사용
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Member> query = cb.createQuery(Member.class);
//            Root<Member> m = query.from(Member.class);
//            CriteriaQuery<Member> cg = query.select(m).where(cb.equal(m.get("username"), "kim"));
//
//            List<Member> resultList = em.createQuery(cg).getResultList();

            // NativeSQL
//            List resultList = em.createNativeQuery("selct member_id, city, from member").getResultList();

            Book book = new Book();
            book.setName("JPA");
            em.persist(book);

            List<Item> resultList = em.createQuery("""
                        select 
                            i
                        from Item i where type(i) = Book
                    """, Item.class).getResultList();

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
