package GongMoa.gongmoa.domain.chat;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.service.chat.TimeSaveClass;
import lombok.Getter;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Table(name = "ChatMessage")
public class ChatMessage implements TimeSaveClass {
    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    private String message;

    private LocalDateTime time;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    public ChatMessage() {
    }

    public ChatMessage(String message, LocalDateTime time, User writer, ChatRoom chatRoom) {
        this.message = message;
        this.time = time;
        this.writer = writer;
        this.chatRoom = chatRoom;
    }
}
