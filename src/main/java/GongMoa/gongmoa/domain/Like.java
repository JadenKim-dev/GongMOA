package GongMoa.gongmoa.domain;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.Contest.Contest;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Table(name="likes")
public class Like {
    @Id @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private LikeType likeType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    public Like() {
    }

    public Like(User user, LikeType likeType, Contest contest, Notification notification) {
        this.user = user;
        this.likeType = likeType;
        this.contest = contest;
        this.notification = notification;
    }
}
