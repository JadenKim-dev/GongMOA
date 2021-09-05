package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.LoginUser;
import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.*;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.domain.form.NotificationCreateForm;
import GongMoa.gongmoa.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static GongMoa.gongmoa.domain.Notification.isDuplicatedRegistration;

@Controller
//@ResponseBody
@RequestMapping("contests/{contestId}/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final ContestService contestService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final TeamService teamService;
    private final AuthorizationService authorizationService;

    @GetMapping
    public String listNotifications(@PathVariable long contestId, Model model) {
        Contest contest = contestService.findContest(contestId);
        List<Notification> notifications = notificationService.SearchNotificationsByContestId(contest);

        model.addAttribute("contest", contest);
        model.addAttribute("notifications", notifications);
        return "notifications";
    }

    @GetMapping("/create")
    public String notificationCreateForm(@PathVariable long contestId, Model model) {
        Contest contest = contestService.findContest(contestId);

        model.addAttribute("form", new Notification());
        model.addAttribute("contest", contest);
        return "createNotificationForm";
    }

    @PostMapping("/create")
    public String createNotificationAndTeam(@Validated @ModelAttribute("form") NotificationCreateForm form,
                                            BindingResult bindingResult,
                                            @PathVariable long contestId,
                                            @LoginUser SessionUser user,
                                            Model model,
                                            HttpServletRequest request) {
        Contest contest = contestService.findContest(contestId);
        User currentUser = userService.findUser(user.getId());

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {} ", bindingResult);
            return "redirect:/contests/{contestId}/notifications/create";
        }

        Long notificationId = notificationService.createNotification(currentUser, form.getTitle(), form.getDescription(), contest);
        request.setAttribute("notificationId", notificationId);
        request.setAttribute("contestId", contestId);
        return "forward:/teams";
    }

    @GetMapping("/{notificationId}")
    public String notification(
            @PathVariable long contestId,
            @PathVariable long notificationId,
            @LoginUser SessionUser user,
            Model model) {
        User currentUser = userService.findUser(user.getId());
        Contest contest = contestService.findContest(contestId);
        Notification notification = notificationService.findNotification(notificationId);
        List<Registration> registrations = notification.getRegistrations();
        Registration currentRegistration = registrations.stream().
                filter(r -> r.getUser().equals(currentUser)).findFirst().orElse(null);

        Team team = teamService.findTeamByNotification(notification);

        model.addAttribute("contest", contest);
        model.addAttribute("notification", notification);
        model.addAttribute("currentRegistration", currentRegistration);
        model.addAttribute("registrations", registrations);
        model.addAttribute("teamId", team != null ? team.getId() : null);
        return "notification";
    }

    @DeleteMapping("/{notificationId}")
    public String deleteNotification(
            @LoginUser SessionUser user,
            @PathVariable long notificationId,
            @RequestHeader("Referer") String referer) {

        User currentUser = userService.findUser(user.getId());
        Notification notification = notificationService.findNotification(notificationId);

        if(!authorizationService.authorizeUserIsWriter(currentUser, notification)) {
            return "redirect:" + referer;
        }

        notificationService.deleteNotification(notification);

        return "redirect:/contests/{contestId}/notifications";
    }

    @PostMapping("/{notificationId}/register")
    public String register(@PathVariable long contestId,
                           @PathVariable long notificationId,
                           @LoginUser SessionUser user,
                           @RequestParam String description) {

        Contest contest = contestService.findContest(contestId);
        Notification notification = notificationService.findNotification(notificationId);
        User currentUser = userService.findUser(user.getId());

        if(isDuplicatedRegistration(notification, currentUser)) {
            log.info("Duplicated Registration");
            return "redirect:/contests/{contestId}/notifications/{notificationId}";
        }

        Registration registration = notificationService.register(currentUser, notification, false, description);

        log.info("registration={}", registration);

        return "redirect:/contests/{contestId}/notifications/{notificationId}";
    }

    @PostMapping("/{notificationId}/cancelRegister")
    public String cancelRegister(
            @LoginUser SessionUser user,
            @RequestParam long registrationId,
            @RequestHeader("Referer") String referer) {

        User currentUser = userService.findUser(user.getId());
        Registration registration = Registration.findRegistrationFromUserById(currentUser, registrationId);

        if(registration == null) {
            log.info("registration == null");
            return "redirect:" + referer;
        }

        log.info("registration = ", registration.getId());
        notificationService.cancelRegistration(registration);
        log.info("registrations={}", registration.getNotification().getRegistrations());

        return "redirect:/contests/{contestId}/notifications/{notificationId}";
    }
}