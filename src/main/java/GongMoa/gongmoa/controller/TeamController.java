package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.domain.Member;
import GongMoa.gongmoa.domain.Notification;
import GongMoa.gongmoa.domain.Registration;
import GongMoa.gongmoa.domain.Team;
import GongMoa.gongmoa.service.MemberService;
import GongMoa.gongmoa.service.NotificationService;
import GongMoa.gongmoa.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@ResponseBody
@RequestMapping("/team/{team_id}")
@RequiredArgsConstructor
@Slf4j
public class TeamController {
    private final TeamService teamService;
    private final MemberService memberService;
    private final NotificationService notificationService;


    @PostMapping
    public String join(@PathVariable long team_id) {
        Team team = getTeam(team_id);

        // 요청 데이터로 넘어오는 정보
        Long notificationId = 7L;
        Long registrationId = 10L;

        Notification notification = getNotification(notificationId);
        Member member = findMember(notification, registrationId);

        return "";
    }

    private Member findMember(Notification notification, Long registrationId) {
        List<Registration> registrations = notification.getRegistrations();
        for (Registration registration : registrations) {
            if(registration.getId().equals(registrationId)) {
                return registration.getMember();
            }
        } return null;
    }

    private Team getTeam(long team_id) {
        return teamService.findTeam(team_id).orElseThrow(NoSuchElementException::new);
    }

    private Member getMember(long memberId) {
        return memberService.findMember(memberId).orElseThrow(NoSuchElementException::new);
    }

    private Notification getNotification(long notificationId) {
        return notificationService.findNotification(notificationId).orElseThrow(NoSuchElementException::new);
    }


}
