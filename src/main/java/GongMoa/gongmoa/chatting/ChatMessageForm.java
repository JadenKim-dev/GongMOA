package GongMoa.gongmoa.chatting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageForm {
    private Long chatRoomId;
    private String receiver;
    private String sender;
    private String message;
}
