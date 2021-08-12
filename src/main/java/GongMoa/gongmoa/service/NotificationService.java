package GongMoa.gongmoa.service;

import GongMoa.gongmoa.domain.Notification;
import GongMoa.gongmoa.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Long createNotification(Notification notification) {
        return notificationRepository.save(notification).getId();
    }

    @Transactional
    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
        return;
    }

    public Optional<Notification> findNotification(Long notificationId) {
        return notificationRepository.findById(notificationId);
    }
}
