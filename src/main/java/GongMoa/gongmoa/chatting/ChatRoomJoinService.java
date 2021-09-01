package GongMoa.gongmoa.chatting;

import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomJoinService {
    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    
    // 기본 메서드
    @Transactional(readOnly = true)
    public List<ChatRoomJoin> findByUser(User user) {
        return chatRoomJoinRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomJoin> findByChatRoom(ChatRoom chatRoom) {
        return chatRoomJoinRepository.findByChatRoom(chatRoom);
    }

    @Transactional
    public void delete(ChatRoomJoin chatRoomJoin) {
        chatRoomJoinRepository.delete(chatRoomJoin);
    }

    public User findAnotherUser(ChatRoom chatRoom, Long userId) {
        List<ChatRoomJoin> chatRoomJoins = this.findByChatRoom(chatRoom);
        ChatRoomJoin findChatRoomJoin = chatRoomJoins.stream()
                .filter(j -> j.getUser()!=null && !j.getUser().getId().equals(userId)).findFirst().orElse(null);
        return findChatRoomJoin == null ? null : findChatRoomJoin.getUser();
    }
    
    // 비즈니스 로직
    @Transactional(readOnly = true)
    public long check(String user1name, String user2name) {
        User userFirst = userService.findByName(user1name);
        List<ChatRoomJoin> chatRoomJoinsFirst = chatRoomJoinRepository.findByUser(userFirst);
        Set<ChatRoom> chatRoomsFirst = chatRoomJoinsFirst.stream()
                .map(ChatRoomJoin::getChatRoom).collect(Collectors.toSet());

        User userSecond = userService.findByName(user2name);
        List<ChatRoomJoin> chatRoomJoinsSecond = chatRoomJoinRepository.findByUser(userSecond);

        return chatRoomJoinsSecond.stream()
                .filter(j -> chatRoomsFirst.contains(j.getChatRoom()))
                .map(j -> j.getChatRoom().getId()).findFirst().orElse(0L);
    }

    @Transactional
    public long newRoom(String user1name, String user2name) {
        long result = check(user1name, user2name);
        if(result != 0) {
            return result;
        }
        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom());
        if(user1name.equals(user2name)) {
            createRoom(user1name, chatRoom);
        } else {
            createRoom(user1name, chatRoom);
            createRoom(user2name, chatRoom);
        }
        return chatRoom.getId();
    }

    @Transactional
    public void createRoom(String username, ChatRoom chatRoom) {
        User findUser = userService.findByName(username);
        log.info("username={}", username);
        log.info("findUser={}", findUser);
        if(findUser != null) {
            ChatRoomJoin chatRoomJoin = new ChatRoomJoin(findUser, chatRoom);
            chatRoomJoinRepository.save(chatRoomJoin);
        } else {
            throw new NoSuchElementException();
        }
    }
}
