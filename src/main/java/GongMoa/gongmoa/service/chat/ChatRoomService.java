package GongMoa.gongmoa.service.chat;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.form.ChatRoomForm;
import GongMoa.gongmoa.domain.chat.ChatMessage;
import GongMoa.gongmoa.domain.chat.ChatRoom;
import GongMoa.gongmoa.domain.chat.ChatRoomJoin;
import GongMoa.gongmoa.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    // 기본 메서드
    @Transactional(readOnly = true)
    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> findChatRoomsByUser(User user) {
        return user.getChatRoomJoins().stream().map(ChatRoomJoin::getChatRoom).collect(Collectors.toList());
    }


    // 비즈니스 로직
    @Transactional
    public void deleteEmptyChatRoomOfUser(User user) {
        List<ChatRoom> chatRooms = findChatRoomsByUser(user);
        chatRooms.stream().filter(r -> r.getMessages().size() == 0).forEach(chatRoomRepository::delete);
    }

    public List<ChatRoomForm> convertChatRoomsToForms(List<ChatRoom> chatRooms, User currentUser) {
        List<ChatRoomForm> chatRoomForms = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            User anotherUser = this.findAnotherUserInChatRoom(chatRoom, currentUser);
            ChatMessage lastMessage = this.getLastMessageInChatRoom(chatRoom);
            chatRoomForms.add(
                    new ChatRoomForm(chatRoom.getId(), anotherUser.getName(), anotherUser.getPicture().getStoreFileName(),
                            lastMessage.getMessage(), lastMessage.getTime())
            );
        }

        chatRoomForms.sort(new TimeComparator());
        return chatRoomForms;
    }

    @Transactional(readOnly = true)
    public User findAnotherUserInChatRoom(ChatRoom chatRoom, User currentUser) {
        ChatRoomJoin findChatRoomJoin = chatRoom.getChatRoomJoins().stream()
                .filter(j -> !j.getUser().equals(currentUser)).findFirst().orElse(null);
        return findChatRoomJoin == null ? null : findChatRoomJoin.getUser();
    }

    protected ChatMessage getLastMessageInChatRoom(ChatRoom chatRoom) {
        chatRoom.getMessages().sort(new TimeComparator());
        return chatRoom.getMessages().get(0);
    }

    static class TimeComparator implements Comparator<TimeSaveClass> {
        @Override
        public int compare(TimeSaveClass m1, TimeSaveClass m2) {
            if(m1.getTime().isAfter(m2.getTime())) return -1;
            else return 1;
        }
    }

    @Transactional
    public long getOrCreateChatRoomOfUsers(User user1, User user2) {
        ChatRoom chatRoom = this.getChatRoomOfUsers(user1, user2);

        if(chatRoom != null) {
            return chatRoom.getId();
        }
        return this.createRoom(user1, user2);
    }

    @Transactional(readOnly = true)
    protected ChatRoom getChatRoomOfUsers(User user1, User user2) {
        Set<ChatRoom> chatRooms1 = user1.getChatRoomJoins().stream()
                .map(ChatRoomJoin::getChatRoom).collect(Collectors.toSet());

        Set<ChatRoom> chatRooms2 = user2.getChatRoomJoins().stream()
                .map(ChatRoomJoin::getChatRoom).collect(Collectors.toSet());

        return chatRooms2.stream()
                .filter(chatRooms1::contains)
                .findFirst().orElse(null);
    }

    @Transactional
    protected long createRoom(User user1, User user2) {
        if(user1.equals(user2)) {
            throw new RuntimeException("서로 다른 유저로 채팅방을 만들어야 합니다.");
        }
        ChatRoom chatRoom = new ChatRoom();
        ChatRoomJoin chatRoomJoin1 = new ChatRoomJoin(user1, chatRoom);
        ChatRoomJoin chatRoomJoin2 = new ChatRoomJoin(user2, chatRoom);
        user1.getChatRoomJoins().add(chatRoomJoin1);
        user2.getChatRoomJoins().add(chatRoomJoin2);

        return chatRoomRepository.save(chatRoom).getId();
    }

}
