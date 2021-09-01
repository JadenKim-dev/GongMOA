package GongMoa.gongmoa.chatting;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomForm implements TimeSaveClass{
    private Long id;
    private String writerName;
    private String lastMessage;
    private LocalDateTime time;

    public ChatRoomForm(long id, String writer, String lastMessage, LocalDateTime time) {
        this.id = id;
        this.writerName = writer;
        this.lastMessage = lastMessage;
        this.time = time;
    }
}
