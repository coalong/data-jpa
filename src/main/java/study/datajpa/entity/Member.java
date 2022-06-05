package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
// 생성자는 protected 까지만! private X!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age", "team"})
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String userName;
    private int age;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String userName) {
        this.userName = userName;
    }

    public Member(String userName, int age, Team team) {
        this.userName = userName;
        this.age = age;
        if (team != null) {
        changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this(username, age, null);
    }

    public void changeTeam(Team team) {
        this.team = team;
        // team 리스트에서도 member 추가 해준다.
        team.getMembers().add(this);
    }
}
