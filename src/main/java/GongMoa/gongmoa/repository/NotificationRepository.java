package GongMoa.gongmoa.repository;

import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByContest(Contest contest);
}
