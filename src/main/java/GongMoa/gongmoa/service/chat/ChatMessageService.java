package GongMoa.gongmoa.service.chat;

import GongMoa.gongmoa.domain.form.ChatMessageForm;
import GongMoa.gongmoa.domain.chat.ChatMessage;
import GongMoa.gongmoa.repository.chat.ChatMessageRepository;
import GongMoa.gongmoa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @Transactional
    public void save(ChatMessageForm messageForm) {
        ChatMessage chatMessage = new ChatMessage(
                messageForm.getMessage(),
                messageForm.getTime(),
                userService.findByName(messageForm.getSender()),
                chatRoomService.findById(messageForm.getChatRoomId())
        );
        chatMessageRepository.save(chatMessage);
    }
}
