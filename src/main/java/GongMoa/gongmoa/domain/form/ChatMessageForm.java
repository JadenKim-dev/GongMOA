package GongMoa.gongmoa.domain.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageForm {
    private Long chatRoomId;
    private String receiver;
    private String sender;
    private String message;
    private LocalDateTime time;
}
