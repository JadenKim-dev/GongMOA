package GongMoa.gongmoa.controller;

import GongMoa.gongmoa.domain.Notification;
import GongMoa.gongmoa.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.NoSuchElementException;

@Controller
@ResponseBody
@RequestMapping("/contests/{contestId}/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public String notifications() {
//        notificationService   // contestId를 이용한 조회 로직
        return "";
    }

    @GetMapping("/{notificationId}")
    public String notification(@PathVariable long notificationId) {
        Notification notification = notificationService.findNotification(notificationId).orElseThrow(NoSuchElementException::new);
    }
}
