package GongMoa.gongmoa;


import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.Contest.ApplicationDate;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.domain.Contest.Host;
import GongMoa.gongmoa.domain.Contest.Prize;

import GongMoa.gongmoa.domain.Notification;
import GongMoa.gongmoa.domain.Registration;
import GongMoa.gongmoa.repository.ContestRepository;
import GongMoa.gongmoa.repository.NotificationRepository;
import GongMoa.gongmoa.service.NotificationService;
import GongMoa.gongmoa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final ContestRepository contestRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    @PostConstruct
    @Transactional
    public void init() {

        ApplicationDate applicationDate = new ApplicationDate(LocalDate.of(2019, 12, 25), LocalDate.now());
        Host host = new Host("카카오", "네이버", "과학기술부");
        Prize prize = new Prize("1000만원", "2억원", "취업");

        Contest firstContest = new Contest("첫번째 공모전", "1", null, applicationDate, host, prize);
        Contest secondContest = new Contest("두번째 공모전", "2", null, applicationDate, host, prize);
        Contest thridContest = new Contest("세번째 공모전", "3", null, applicationDate, host, prize);


        contestRepository.save(firstContest);
        contestRepository.save(secondContest);
        contestRepository.save(thridContest);

        Notification notification1 = new Notification("첫번째 구인 공지", "첫번째 공모전에 대한", firstContest, true);
        Notification notification2 = new Notification("두번째 구인 공지", "첫번째 공모전에 대한", firstContest, true);
        Notification notification3 = new Notification("세번째 구인 공지", "두번째 공모전에 대한", secondContest, true);

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);
    }

}
