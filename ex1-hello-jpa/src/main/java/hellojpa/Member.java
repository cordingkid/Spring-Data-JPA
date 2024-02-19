package hellojpa;

import jakarta.persistence.*;

import java.util.Date;


// 만약 Member 클래스가 실제 DB에서 USER라는 테이블일 경우 매핑 @Table(name = "USER")
// 해당 어노테이션 추가
@Entity // (name = "Member") 이런게 있는데 일반적으로 쓸일은 없다 내부적으로 구분하는 이유로쓰임 그냥 기본값쓰면됨
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ", // 매핑할 데이터베이스 시퀀스 이름
        initialValue = 1, allocationSize = 1 // allocationSize 기본값 50
)
public class Member {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(name = "name", nullable = false) // db컬럼명은 name이다.
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING) // 이넘 타입은 db에서 없기 때문에 이런걸 쓸떄 사용함
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP) // 자바의 date는 날짜 시간 다포함인데 db는 날짜, 시간, 날짜 시간 따로기때문에 준다.
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob // clob, blob 같은거 사용할 때 사용
    private String description;

    public Member(){}

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
