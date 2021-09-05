package GongMoa.gongmoa.domain.chat;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatRoom {
    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomJoin> chatRoomJoins = new ArrayList<>();

    public List<ChatMessage> getSortedMessages() {
        this.messages.sort((m1, m2) -> {
            if (m1.getId() > m2.getId()) return -1;
            else return 1;
        });
        return this.messages;
    }

}
