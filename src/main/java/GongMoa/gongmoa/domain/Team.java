package GongMoa.gongmoa.domain;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.Contest.Contest;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Team {
    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    // 생성자
    public Team() {
    }

    public Team(Contest contest) {
        this.contest = contest;
    }

    // 생성 메서드
    public static Participation createParticipation(User user, Team team, boolean isLeader) {
        Participation participation = new Participation(user, team, isLeader);
        team.getParticipants().add(participation);
        return participation;
    }

}
