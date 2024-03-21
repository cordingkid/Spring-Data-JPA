package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
@ToString(of = {"id", "username", "age"}) // 안하는게 좋은데 연습이라 했음
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) { // 원래는 예외를 터쳐야 하는데 예제니까
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
