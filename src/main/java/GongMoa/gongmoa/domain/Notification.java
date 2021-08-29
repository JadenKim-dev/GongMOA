package GongMoa.gongmoa.domain;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.Contest.Contest;
import lombok.Getter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
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

    @OneToMany(mappedBy = "notification", cascade = ALL, orphanRemoval = true)
    private List<Registration> registrations = new ArrayList<>();

    @OneToMany(mappedBy = "notification", cascade = ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public Notification() {
    }

    public Notification(String title, String description, Contest contest, boolean isRecruiting) {
        this.title = title;
        this.description = description;
        this.contest = contest;
        this.isRecruiting = isRecruiting;
    }

    // 생성 메서드
    public static Registration createRegistration(User user, Notification notification, boolean isWriter, String description) {
        Registration registration = new Registration(user, notification, isWriter, description);
        notification.getRegistrations().add(registration);
        return registration;
    }

    // 연관관계 메서드
    public void addLike(Like like) {
        this.likes.add(like);
    }

    public Like findLikeByUser(User user) {
        return likes.stream().filter(l -> l.getUser().equals(user)).findFirst().orElse(null);
    }
}
