package GongMoa.gongmoa.domain;

import GongMoa.gongmoa.OAuth2.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Participation {

    @Id @GeneratedValue
    @Column(name = "participation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private boolean isLeader;

    // 생성자
    public Participation(User user, Team team, boolean isLeader) {
        this.user = user;
        this.team = team;
        this.isLeader = isLeader;
    }

    public Participation() {
    }

    @Override
    public String toString() {
        return user.getName();
    }

}
