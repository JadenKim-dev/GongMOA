package GongMoa.gongmoa.domain;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.BaseEntity.DateBaseEntity;
import lombok.Getter;

import javax.persistence.*;
import javax.transaction.Transactional;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Registration extends DateBaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    private boolean isWriter;

    public Registration() {
    }

    public Registration(User user, Notification notification, boolean isWriter) {
        this.user = user;
        this.notification = notification;
        this.isWriter = isWriter;
    }

    @Override
    public String toString() {
        return user.getName();
    }

    // == 연관관계 메서드 == //

    // == 생성 메서드 == //

}
