package hellojpa;

import jakarta.persistence.*;

@Entity
public class Locker {
    @Id @GeneratedValue
    @Column(name = "locker_id")
    private Long id;

    private String name;

//    @OneToOne(mappedBy = "locker") // 일대일 양방향매핑
//    private Member member;
}
