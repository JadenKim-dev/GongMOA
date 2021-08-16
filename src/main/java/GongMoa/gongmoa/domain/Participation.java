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

    private boolean isLeader;

    // 생성자
    public Participation(Member member, Team team, boolean isLeader) {
        this.member = member;
        this.team = team;
        this.isLeader = isLeader;
    }

    public Participation() {
    }

    @Override
    public String toString() {
        return member.getUsername();
    }
}
