package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        // 이런식으로 저장하는 이유 처음에는 아이디가 없다. 그래서 퍼시스트쓰고
        if (item.getId() == null) { // 새로 생성한 객체
            em.persist(item);
        }else{ // 아이디가 있으면 변경 해주는거
            em.merge(item); // 업데이트 비슷한거?
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
