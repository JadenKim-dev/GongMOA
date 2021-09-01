package GongMoa.gongmoa.chatting;

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
        ChatMessage chatMessage = new ChatMessage(messageForm.getMessage(),
                LocalDateTime.now(),
                userService.findByName(messageForm.getSender()),
                chatRoomService.findById(messageForm.getChatRoomId()));
        chatMessageRepository.save(chatMessage);
    }
}
