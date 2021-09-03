package GongMoa.gongmoa.domain.form;

import GongMoa.gongmoa.service.chat.TimeSaveClass;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomForm implements TimeSaveClass {
    private Long id;
    private String writerName;
    private String profileImg;
    private String lastMessage;
    private LocalDateTime time;

    public ChatRoomForm(long id, String writer, String profileImg, String lastMessage, LocalDateTime time) {
        this.id = id;
        this.writerName = writer;
        this.profileImg = profileImg;
        this.lastMessage = lastMessage;
        this.time = time;
    }
}
