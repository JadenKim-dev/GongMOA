package GongMoa.gongmoa.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Participation {

    @Id @GeneratedValue
    @Column(name = "participation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Participation() {

    }

    public Participation(Member member, Team team) {
        this.member = member;
        this.team = team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
