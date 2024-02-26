package jpql;

import jakarta.persistence.*;

@Entity
@Table(name = "orders") // order 예약어라 관레상
public class Order {
    @Id @GeneratedValue
    private Long id;
    private int orderAmount;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
