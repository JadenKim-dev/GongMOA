package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.domain.*;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.service.ContestService;
import GongMoa.gongmoa.service.MemberService;
import GongMoa.gongmoa.service.NotificationService;
import GongMoa.gongmoa.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@ResponseBody
@RequestMapping("/team")
@RequiredArgsConstructor
@Slf4j
public class TeamController {
    private final TeamService teamService;
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final ContestService contestService;

    @PostMapping
    public String createTeam() {

        // 요청 데이터로 넘어오는 정보
        Long memberId = 1L;
        Long notificationId = 7L;

        Member member = getMember(memberId);
        Notification notification = getNotification(notificationId);

        Contest contest = getContest(notification.getContest().getId());

        Long team = teamService.createTeam(member, contest);

        return team.toString();
    }

    @DeleteMapping("/{team_id}")
    public String deleteTeam(@PathVariable long team_id) {
        Team team = getTeam(team_id);

        teamService.deleteTeam(team);

        return "ok";
    }

    @PostMapping("/{team_id}")
    public String join(@PathVariable long team_id) {
        Team team = getTeam(team_id);

        // 요청 데이터로 넘어오는 정보
        Long notificationId = 7L;
        Long registrationId = 12L;

        Notification notification = getNotification(notificationId);
        Member member = findMemberFromNotification(notification, registrationId);

        teamService.join(member, team);

        return "";
    }

    @PostMapping("/{team_id}/leave")
    public String leaveTeam(@PathVariable long team_id) {
        Team team = getTeam(team_id);

        // 요청 데이터로 넘어오는 정보
        Long participationId = 13L;

        teamService.leaveTeam(participationId, team);
        return "ok";
    }

    private Member findMemberFromNotification(Notification notification, Long registrationId) {
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

    private Contest getContest(long contest_id) {
        return contestService.findContest(contest_id).orElseThrow(NoSuchElementException::new);
    }


}
