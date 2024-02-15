package hellojpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


// 만약 Member 클래스가 실제 DB에서 USER라는 테이블일 경우 매핑 @Table(name = "USER")
// 해당 어노테이션 추가
@Entity
public class Member {

    @Id // pk
    private Long id;

    // 실제 DB에서 name이 username일 경우 매핑 하기 @Column(name = "username")
    private String name;

    public Member(){}

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
