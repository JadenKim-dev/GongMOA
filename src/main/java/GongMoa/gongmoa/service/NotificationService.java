package GongMoa.gongmoa.service;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.domain.Like;
import GongMoa.gongmoa.domain.LikeType;
import GongMoa.gongmoa.domain.Notification;
import GongMoa.gongmoa.domain.Registration;
import GongMoa.gongmoa.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Long createNotification(User writer, String title, String description, Contest contest) {
        Notification notification = new Notification(title, description, contest, true);
        this.register(writer, notification, true, null);
        return notificationRepository.save(notification).getId();
    }

    @Transactional
    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
        return;
    }

    public Notification findNotification(Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(NoSuchElementException::new);
    }

    public List<Notification> SearchNotificationsByContestId(Contest contest) {
        return notificationRepository.findByContest(contest);
    }

    @Transactional
    public Registration register(User user, Notification notification, boolean isWriter, String description) {
        return Notification.createRegistration(user, notification, isWriter, description);
    }

    @Transactional
    public void cancelRegistration(Registration registration) {
        Notification notification = registration.getNotification();
        notification.getRegistrations().remove(registration);
    }

    @Transactional
    public void likeNotification(User user, Notification notification) {
        Like like = new Like(user, LikeType.NOTIFICATION, null, notification);
        notification.getLikes().add(like);
    }

    @Transactional
    public void cancelLikeInNotification(User user, Notification notification) {
        Like like = notification.findLikeByUser(user);
        log.info("like={}", like);
        notification.getLikes().remove(like);
    }
}
