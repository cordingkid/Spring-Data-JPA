package hellojpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;


// 만약 Member 클래스가 실제 DB에서 USER라는 테이블일 경우 매핑 @Table(name = "USER")
// 해당 어노테이션 추가
@Entity // (name = "Member") 이런게 있는데 일반적으로 쓸일은 없다 내부적으로 구분하는 이유로쓰임 그냥 기본값쓰면됨
public class Member{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username", nullable = false) // db컬럼명은 name이다.
    private String username;

    // Period 기간
    @Embedded
    private Period period;

    // 주소
    @Embedded
    private Address homeAddress;

    @ElementCollection // 값 타입 컬렉션
    @CollectionTable(
            name = "favorite_food",
            joinColumns = @JoinColumn(name = "member_id")
    )
    @Column(name = "food_name")
    private Set<String> favoriteFoods = new HashSet<>();

//    @ElementCollection // 값 타입 컬렉션
//    @CollectionTable(
//            name = "address",
//            joinColumns = @JoinColumn(name = "member_id")
//    )
//    private List<Address> addressHistory = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<AddressEntity> addressHistory = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }
}
