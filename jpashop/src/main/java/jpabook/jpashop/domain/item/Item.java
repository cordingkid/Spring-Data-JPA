package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // == 비즈니스 로직 == //

    /*
        재고를 늘리고 줄이는 로직 추가
        왜 여기에 넣냐? 도메인 주도 설계라고 할때
        엔티티 자체가 해결할 수 있는 것들이나
        그럴때는 주로 엔티티 안에 비즈니스 로직을 넣는게 좋다!
        응집도 높다!

        여기서 밖에서 계산해서 set 해서 넣는게 아니고
        setter를 지운다음 이안에 비즈니스 로직 생성해서 addstock한다던지 remove 한다던지 해야함
        그게 가장 객체 지향적이다!
     */

    /**
     * stock(재고) 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock(재고) 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
