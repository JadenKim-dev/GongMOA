package GongMoa.gongmoa.controller.chat;

import GongMoa.gongmoa.domain.form.ChatMessageForm;
import GongMoa.gongmoa.service.chat.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/send")
    public void sendMsg(@ModelAttribute ChatMessageForm message) {
        message.setTime(LocalDateTime.now());
        String receiver = message.getReceiver();
        chatMessageService.save(message);
        simpMessagingTemplate.convertAndSend("/topic/" + receiver, message);
    }
}
