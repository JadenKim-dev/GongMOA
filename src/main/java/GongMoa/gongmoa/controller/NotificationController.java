package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.OAuth2.LoginUser;
import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.*;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.domain.form.NotificationCreateForm;
import GongMoa.gongmoa.service.ContestService;
import GongMoa.gongmoa.service.NotificationService;
import GongMoa.gongmoa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
//@ResponseBody
@RequestMapping("contests/{contestId}/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final ContestService contestService;
    private final NotificationService notificationService;
    private final UserService userService;  // 테스트 데이터 사용을 위함

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
    public String createNotification(@Validated @ModelAttribute("form") NotificationCreateForm form,
                                     BindingResult bindingResult,
                                     @PathVariable long contestId,
                                     @LoginUser SessionUser user) {
        Contest contest = contestService.findContest(contestId);
        User currentUser = userService.findUser(user.getId());

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {} ", bindingResult);
            return "contests/{contestId}/notifications/create";
        }

        Long notificationId = notificationService.createNotification(currentUser, form.getTitle(), form.getDescription(), contest);
        return "redirect:/contests/{contestId}/notifications";
    }

    @GetMapping("/{notificationId}")
    public String notification(
            @PathVariable long contestId,
            @PathVariable long notificationId,
            Model model) {
        Contest contest = contestService.findContest(contestId);
        Notification notification = notificationService.findNotification(notificationId);
        List<Registration> registrations = notification.getRegistrations();

        model.addAttribute("contest", contest);
        model.addAttribute("notification", notification);
        model.addAttribute("registrations", registrations);
        return "notification";
    }

    @PostMapping("/{notificationId}/register")

    public String register(@PathVariable long contestId,
                           @PathVariable long notificationId,
                           @LoginUser SessionUser user,
                           String description) {
        Contest contest = contestService.findContest(contestId);
        Notification notification = notificationService.findNotification(notificationId);
        User currentUser = userService.findUser(user.getId());

        Registration registration = notificationService.register(currentUser, notification, false, description);
        log.info("registration={}", registration);

        return "redirect:/contests/{contestId}/notifications";
    }
}