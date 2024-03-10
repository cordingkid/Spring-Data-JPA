package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    /*
        이게 좋은 테스트라고는 볼 수 없다.
        왜냐하면 좋은 테스트는 DB랑 이런것이 Dependency 없이 정말 Spring도
        안엮고 정말 딱 순수하게 그 매서드를 단위 테스트 하는게 좋은데

        여기서는 JPA나 이런걸 엮어서 동작하는거가 전체적으로 잘 되는지 보는것이 목적이기때문에
        통합으로 해서 테스트를 작성한다.
     */

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    
    @Test
    public void 상품주문() throws Exception{
        // given
        Member member = createMember();

        Item book = createBook("시골 JPA", 10_000, 10);


        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        // assertEquals 파라미터 1.기대값 2.실제값 3.메세지
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10_000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception{
        // given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10_000, 10);

        int orderCount = 11;

        // when
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        }, "재고 수량 예외가 발행해야 한다."); // 만약 위에서 발생 안하면 에러 발생한다.
        // 사실은 이렇게 통합테스트 하는것 보다 아이템 엔티티에 정의한 비즈니스로직을 단위 테스트 하는게 더 좋다!

        // then
//        fail("재고 수량 예외가 발행해야 한다.");
    }

    @Test
    public void 주문취소() throws Exception{
        // given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10_000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);
        
        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL 이다.");
        assertEquals(10, book.getStockQuantity(), "주문 최소된 상품은 그만큼 재고가 증가해야 한다.");
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name); // 영역 선택후 Ctrl + alt + p 누르면 파라미터로 꺼낼수 있다.
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        // 컨트롤 알트 M 누르면 만들어짐
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "마포대교밑", "123-123"));
        em.persist(member);
        return member;
    }
}