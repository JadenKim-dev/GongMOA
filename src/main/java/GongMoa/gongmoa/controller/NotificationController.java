package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.domain.*;
import GongMoa.gongmoa.domain.Contest.Contest;
import GongMoa.gongmoa.domain.form.NotificationCreateForm;
import GongMoa.gongmoa.repository.MemberRepository;
import GongMoa.gongmoa.service.ContestService;
import GongMoa.gongmoa.service.MemberService;
import GongMoa.gongmoa.service.NotificationService;
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
    private final MemberService memberService;  // 테스트 데이터 사용을 위함

    @GetMapping
    public String listNotifications(@PathVariable long contestId, Model model) {
        Contest contest = getContest(contestId);
        List<Notification> notifications = notificationService.SearchNotificationsByContestId(contest);
        model.addAttribute("notifications", notifications);
        return "notifications";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", new Notification());
        return "createNotificationForm";
    }

    @PostMapping("/create")
    public String createNotification(@Validated @ModelAttribute("form") NotificationCreateForm form,
                                     BindingResult bindingResult,
                                     @PathVariable long contestId) {
        Contest contest = getContest(contestId);

        // Form을 통해 들어올 정보
        String title = "공고A";
        String description = "AA";

        // 자동으로 들어올 정보
        Member member = getMember(1L);

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {} ", bindingResult);
            return "contests/{contestId}/notifications/create";
        }

        Long notificationId = notificationService.createNotification(member, form.getTitle(), form.getDescription(), contest);
        return "redirect:/contests/{contestId}";
    }

    @GetMapping("/{notificationId}")
    public String notification(@PathVariable long notificationId) {
        Notification notification = getNotification(notificationId);
        List<Registration> registrations = notification.getRegistrations();
        return notification.toString() + '\n'
                + registrations.toString();
    }

    @PostMapping("/{notificationId}/register")
    public String register(@PathVariable long contestId, @PathVariable long notificationId) {
        Contest contest = getContest(contestId);
        Notification notification = getNotification(notificationId);
        Member member = getMember(1L);

        Registration registration = notificationService.register(member, notification, false);
        log.info("registration={}", registration);

        return "redirect:/contests/{contestId}/notifications";
    }

    private Contest getContest(long contestId) {
        return contestService.findContest(contestId).orElseThrow(NoSuchElementException::new);
    }

    private Notification getNotification(long notificationId) {
        return notificationService.findNotification(notificationId).orElseThrow(NoSuchElementException::new);
    }

    // 테스트 데이터 조회용
    private Member getMember(long memberId) {
        return memberService.findMember(memberId).orElseThrow(NoSuchElementException::new);
    }

}
