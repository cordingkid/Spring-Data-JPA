package jpabook.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    /**
     * 재사용성이 떨어지지만 페치조인보다 '아주 미세'하게 더 성능이 좋음
     * 그리고 API 스팩이 바뀌면 이걸 뜯어 고쳐야 한다는 단점이 있다.
     */
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery("""
                        select 
                            new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)
                        from Order o
                        join o.member m
                        join o.delivery d
                        """, OrderSimpleQueryDto.class)
                .getResultList();
    }
}
