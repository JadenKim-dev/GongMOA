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

    @OneToMany(mappedBy = "notification", cascade = ALL)
    private List<Registration> registrations = new ArrayList<>();

    public Notification() {
    }

    public Notification(String title, String description, Contest contest, boolean isRecruiting) {
        this.title = title;
        this.description = description;
        this.contest = contest;
        this.isRecruiting = isRecruiting;
    }


    // 생성 메서드
    public static Registration createRegistration(User user, Notification notification, boolean isWriter) {
        Registration registration = new Registration(user, notification, isWriter);
        notification.getRegistrations().add(registration);
        return registration;
    }

}
