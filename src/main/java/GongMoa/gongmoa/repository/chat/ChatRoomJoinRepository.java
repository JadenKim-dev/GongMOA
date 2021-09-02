package GongMoa.gongmoa.repository.chat;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.chat.ChatRoom;
import GongMoa.gongmoa.domain.chat.ChatRoomJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {
    List<ChatRoomJoin> findByUser(User user);

    List<ChatRoomJoin> findByChatRoom(ChatRoom chatRoom);
}
