package GongMoa.gongmoa.domain;

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

    private String teamName;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Participation> participants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    public void addParticipant(Member member) {
        Participation participant = new Participation(member, this);
        this.getParticipants().add(participant);
    }

    public void deleteParticipant(Participation participant) {
        this.getParticipants().remove(participant);
    }

}
