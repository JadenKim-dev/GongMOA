package GongMoa.gongmoa.domain;

import GongMoa.gongmoa.domain.Contest.Contest;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Lob @Basic(fetch = LAZY)
    private String description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    private boolean isRecruiting;

}
