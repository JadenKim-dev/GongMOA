package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.LoginUser;
import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.*;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
@Slf4j
public class TeamController {
    private final TeamService teamService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final ContestService contestService;
    private final AuthorizationService authorizationService;

    @PostMapping
    public String createTeam(@LoginUser SessionUser user, @RequestParam("notificationId") long notificationId,
                             @RequestHeader("Referer") String referer) {

        User currentUser = userService.findUser(user.getId());
        Notification notification = notificationService.findNotification(notificationId);

        if(!authorizationService.authorizeUserIsWriter(currentUser, notification)) {
            return "redirect:" + referer;
        }

        Contest contest = contestService.findContest(notification.getContest().getId());
        Long teamId = teamService.createTeam(currentUser, contest, notification);

        return "redirect:/teams/" + teamId;
    }

    @GetMapping("/{teamId}")
    public String team(@LoginUser SessionUser user, @PathVariable long teamId, Model model) {
        User currentUser = userService.findUser(user.getId());
        Team team = teamService.findTeam(teamId);
        if(!authorizationService.authorizeUserIsTeamParticipant(currentUser, team)){
            log.info("!authorizationService.authorizeUserIsTeamParticipant");
            return "redirect:/contests/" + team.getNotification().getContest().getId()
                    + "/notifications/" + team.getNotification().getId();
        }
        model.addAttribute("team", team);
        return "team";
    }
  
    @DeleteMapping("/{teamId}")
    public String deleteTeam(@LoginUser SessionUser user,
                             @RequestHeader("Referer") String referer,
                             @PathVariable long teamId) {

        User currentUser = userService.findUser(user.getId());
        Team team = teamService.findTeam(teamId);

        if(!authorizationService.authorizeUserIsLeader(currentUser, team)) {
            log.info("referer={}", referer);
            return "redirect:" + referer;
        }

        teamService.deleteTeam(team);

        return "redirect:/contests";
    }

    @PostMapping("/{teamId}")
    public String join(@PathVariable long teamId,
                       @RequestParam long userId) {
        Team team = teamService.findTeam(teamId);
        User user = userService.findUser(userId);

        teamService.join(user, team);

        return "redirect:/teams/{teamId}";
    }

    @PostMapping("/{teamId}/leave")
    public String leaveTeam(
            @LoginUser SessionUser user,
            @PathVariable long teamId) {
        Team team = teamService.findTeam(teamId);

        teamService.leaveTeam(user.getId(), team);

        return "redirect:/teams/{teamId}";
    }
}