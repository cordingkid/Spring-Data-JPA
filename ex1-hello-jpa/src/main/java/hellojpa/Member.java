package hellojpa;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// 만약 Member 클래스가 실제 DB에서 USER라는 테이블일 경우 매핑 @Table(name = "USER")
// 해당 어노테이션 추가
@Entity // (name = "Member") 이런게 있는데 일반적으로 쓸일은 없다 내부적으로 구분하는 이유로쓰임 그냥 기본값쓰면됨
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username", nullable = false) // db컬럼명은 name이다.
    private String username;

    @OneToOne
    @JoinColumn(name = "locker_id")
    private Locker locker;

    @ManyToOne
    @JoinColumn(name = "team_id") // 이렇게 하면 연관관계가된다.
    private Team team;

    /*@ManyToMany // 근데 사실 실무에선 절대 안씀 쓰면안됨!!
    @JoinTable(
            name = "member_product" // 테이블명
    )
    private List<Product> products = new ArrayList<>();*/

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

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

    public Team getTeam() {
        return team;
    }

    public void changeTeam(Team team) { // 로직이 들어가면 set을 안쓰고 이름을 바꾼다.
        this.team = team;
        team.getMembers().add(this); // 연관 관계 편의 매서드
    }
}
