package jpql;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);


            Member member = new Member();
            member.setUsername("회원1");
            member.setAge(10);
            member.setType(MemberType.ADMIN);
            member.setTeam(teamA);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(10);
            member2.setType(MemberType.ADMIN);
            member2.setTeam(teamA);

            Member member3 = new Member();
            member3.setUsername("회원2");
            member3.setAge(10);
            member3.setType(MemberType.ADMIN);
            member3.setTeam(teamB);


            em.persist(member);
            em.persist(member2);
            em.persist(member3);

            em.flush();
            em.clear();

            // 벌크 연산 예제 FLUSH 자동 호출
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();


//            List<Team> resultList = em.createQuery("""
//                    select
//                        t
//                    from Team t
//                    join fetch t.members
//                    """, Team.class)
//                    .getResultList();
//
//            System.out.println(resultList.size());
//
//            for (Team t : resultList) {
//                System.out.println("team = " + t.getName() + "members=>" + t.getMembers());
//            }

            // 네임드 쿼리 사용법
//            em.createNamedQuery("Member.findByUsername", Member.class)
//                            .setParameter("username", "회원1")
//                            .getResultList();

//            System.out.println("resultList = " + resultList);


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
