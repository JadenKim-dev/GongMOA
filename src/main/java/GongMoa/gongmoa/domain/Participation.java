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

    // == 연관관계 메서드 == //
    public void setMember(Member member) {
        this.member = member;
//        member.getParticipations().add(this);
    }

    public void setTeam(Team team) {
        
    }


    // == 생성 메서드 == //




}
