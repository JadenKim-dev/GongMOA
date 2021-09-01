package GongMoa.gongmoa.chatting;

import GongMoa.gongmoa.OAuth2.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJoinService chatRoomJoinService;

    @Transactional(readOnly = true)
    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id).orElse(null);
    }

    public List<ChatRoomForm> setting(List<ChatRoomJoin> chatRoomJoins, User user) {
        List<ChatRoomForm> chatRoomForms = new ArrayList<>();
        for (ChatRoomJoin chatRoomJoin : chatRoomJoins) {
            ChatRoom chatRoom = chatRoomJoin.getChatRoom();
            if(chatRoom.getMessages().size() != 0) {
                Collections.sort(chatRoom.getMessages(), new TimeComparator());
                ChatMessage lastMessage = chatRoom.getMessages().get(0);
                User anotherUser = chatRoomJoinService.findAnotherUser(chatRoom, user.getId());
                chatRoomForms.add(new ChatRoomForm(
                        chatRoom.getId(),
                        anotherUser==null ? null : anotherUser.getName(),
                        lastMessage.getMessage(),
                        lastMessage.getTime())
                );
            } else {
                chatRoomJoinService.delete(chatRoomJoin);
            }
        }
        Collections.sort(chatRoomForms, new TimeComparator());

        return chatRoomForms;
    }

    static class TimeComparator implements Comparator<TimeSaveClass> {
        @Override
        public int compare(TimeSaveClass m1, TimeSaveClass m2) {
            if(m1.getTime().isAfter(m2.getTime())) return -1;
            else return 1;
        }
    }

}
