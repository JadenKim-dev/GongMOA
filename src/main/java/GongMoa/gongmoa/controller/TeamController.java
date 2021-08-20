package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.*;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.service.ContestService;
import GongMoa.gongmoa.service.NotificationService;
import GongMoa.gongmoa.service.TeamService;
import GongMoa.gongmoa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
@Slf4j
public class TeamController {
    private final TeamService teamService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final ContestService contestService;

    @PostMapping
    public String createTeam() {

        // 요청 데이터로 넘어오는 정보
        Long userId = 1L;
        Long notificationId = 7L;

        User user = userService.findUser(userId);
        Notification notification = getNotification(notificationId);

        Contest contest = getContest(notification.getContest().getId());

        Long teamId = teamService.createTeam(user, contest);

        return "redirect:/teams/" + teamId;
    }

    @GetMapping("/{teamId}")
    public String team(@PathVariable long teamId, Model model) {
        Team team = getTeam(teamId);
        model.addAttribute("team", team);
        return "team";
    }
  
    @DeleteMapping("/{teamId}")
    public String deleteTeam(@PathVariable long teamId) {
        Team team = getTeam(teamId);

        teamService.deleteTeam(team);

        return "redirect:/contests";
    }

    @PostMapping("/{teamId}")
    public String join(@PathVariable long teamId) {
        Team team = getTeam(teamId);

        // 요청 데이터로 넘어오는 정보
        Long notificationId = 7L;
        Long registrationId = 12L;

        Notification notification = getNotification(notificationId);
        User user = findUserFromNotification(notification, registrationId);

        teamService.join(user, team);

        return "";
    }

    @PostMapping("/{teamId}/leave")
    public String leaveTeam(@PathVariable long teamId) {
        Team team = getTeam(teamId);

        // 요청 데이터로 넘어오는 정보
        Long participationId = 13L;

        teamService.leaveTeam(participationId, team);
        return "ok";
    }

    private User findUserFromNotification(Notification notification, Long registrationId) {
        List<Registration> registrations = notification.getRegistrations();
        for (Registration registration : registrations) {
            if(registration.getId().equals(registrationId)) {
                return registration.getUser();
            }
        } return null;
    }

    private Team getTeam(long teamId) {
        return teamService.findTeam(teamId).orElseThrow(NoSuchElementException::new);
    }



    private Notification getNotification(long notificationId) {
        return notificationService.findNotification(notificationId).orElseThrow(NoSuchElementException::new);
    }

    private Contest getContest(long contest_id) {
        return contestService.findContest(contest_id).orElseThrow(NoSuchElementException::new);
    }


}