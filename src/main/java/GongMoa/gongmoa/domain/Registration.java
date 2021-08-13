package GongMoa.gongmoa.domain;

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
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    private boolean isWriter;

    public Registration() {
    }

    public Registration(Member member, Notification notification, boolean isWriter) {
        this.member = member;
        this.notification = notification;
        this.isWriter = isWriter;
    }

    // == 연관관계 메서드 == //

    // == 생성 메서드 == //

}
