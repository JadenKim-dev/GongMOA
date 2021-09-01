package GongMoa.gongmoa.chatting;

import GongMoa.gongmoa.OAuth2.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ChatRoomJoin {
    @Id @GeneratedValue
    @Column(name = "chat_join_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    public ChatRoomJoin(User user, ChatRoom chatRoom) {
        this.user = user;
        this.chatRoom = chatRoom;
    }

    public ChatRoomJoin() {
    }
}
