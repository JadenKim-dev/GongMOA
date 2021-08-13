package GongMoa.gongmoa;

import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.domain.Member;
import GongMoa.gongmoa.domain.Notification;
import GongMoa.gongmoa.domain.Registration;
import GongMoa.gongmoa.repository.ContestRepository;
import GongMoa.gongmoa.repository.MemberRepository;
import GongMoa.gongmoa.repository.NotificationRepository;
import GongMoa.gongmoa.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final ContestRepository contestRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    @PostConstruct
    public void init() {
        Member memberA = new Member("memberA", "1234", "aa@gmail.com", null);
        Member memberB = new Member("memberB", "1234", "bb@gmail.com", null);
        Member memberC = new Member("memberC", "1234", "cc@gmail.com", null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);

        Contest firstContest = new Contest("첫번째 공모전", "1", null, null, null, null);
        Contest secondContest = new Contest("두번째 공모전", "2", null, null, null, null);
        Contest thridContest = new Contest("세번째 공모전", "3", null, null, null, null);

        contestRepository.save(firstContest);
        contestRepository.save(secondContest);
        contestRepository.save(thridContest);

        Notification notification1 = new Notification("첫번째 구인 공지", "첫번째 공모전에 대한", firstContest, true);
        Notification notification2 = new Notification("두번째 구인 공지", "첫번째 공모전에 대한", firstContest, true);
        Notification notification3 = new Notification("세번째 구인 공지", "두번째 공모전에 대한", secondContest, true);

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);

//        Registration regist1 = notificationService.register(memberA, notification1, true);
//        Registration regist2 = notificationService.register(memberB, notification1, false);
//        Registration regist3 = notificationService.register(memberC, notification1, false);
    }

}
