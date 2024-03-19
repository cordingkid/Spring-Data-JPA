package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jpabook.jpashop.domain.QMember.member;
import static jpabook.jpashop.domain.QOrder.order;

@Repository
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * 검색 기능
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName()
                            + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000
        return query.getResultList();
    }

    /**
     * QueryDSL
     */
    public List<Order> findAll(OrderSearch orderSearch) {
        return query
                .select(order)
                .from(order)
                .join(order.member, member) // as 를 위에 있는 member로 준다.
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName())) // 널이면 버리고 있으면 비교한다.
                .limit(1000)
                .fetch();
    }

    private BooleanExpression nameLike(String memberName) { // 동적 쿼리
        if (!StringUtils.hasText(memberName)) {
            return null;
        }
        return member.name.like(memberName);
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if (statusCond == null) {
            return null;
        }
        return order.status.eq(statusCond);
    }

    /**
     * 재사용성 좋음
     */
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("""
                        select o from Order o
                        join fetch o.member m
                        join fetch o.delivery d
                        """, Order.class)
                .getResultList();
    }

    public List<Order> findAllWithItem() {
        // distinct를 사용해 중복을 제거 해줘야 하는데 하이버네이트 버전이 높아지면서 안써도 해결되었다.
        return em.createQuery("""
                        select o from Order o
                        join fetch o.member m
                        join fetch o.delivery
                        join fetch o.orderItems oi
                        join fetch oi.item i
                        """, Order.class)
                .setFirstResult(1) // 일대다 페치 조인에서는 페이징 하면 안된다 (연습이라 해봄)
                .setMaxResults(100)
                .getResultList();
    }

    /**
     * 일대다 관계 페이징 처리
     */
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery("""
                        select o from Order o
                        join fetch o.member m
                        join fetch o.delivery d
                        """, Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
