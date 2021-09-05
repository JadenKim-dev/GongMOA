package GongMoa.gongmoa.controller.chat;

import GongMoa.gongmoa.OAuth2.LoginUser;
import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.chat.ChatRoom;
import GongMoa.gongmoa.domain.form.ChatRoomForm;
import GongMoa.gongmoa.service.UserService;
import GongMoa.gongmoa.service.chat.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/chatrooms")
    public String listChatRooms(@LoginUser SessionUser user, Model model) {
        User currentUser = userService.findUser(user.getId());
        chatRoomService.deleteEmptyChatRoomOfUser(currentUser);
        List<ChatRoom> chatRooms = chatRoomService.findChatRoomsByUser(currentUser);
        List<ChatRoomForm> chatRoomForms = chatRoomService.convertChatRoomsToForms(chatRooms, currentUser);

        model.addAttribute("chatRooms", chatRoomForms);
        return "chat/chatRooms";
    }

    @PostMapping("/chatrooms")
    public String createChatRoom(@RequestParam("receiver") String userName1, @RequestParam("sender") String userName2) {
        try{
            User user1 = userService.findByName(userName1);
            User user2 = userService.findByName(userName2);

            if(user1==null || user2==null || user1==user2) {
                return "redirect:/chatrooms";
            }

            long chatRoomId = chatRoomService.getOrCreateChatRoomOfUsers(user1, user2);
            return "redirect:/chatrooms/" + chatRoomId;

        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return "redirect:/chatrooms";
        }
    }

    @RequestMapping("/chatrooms/{chatRoomId}")
    public String chatRoom(@PathVariable("chatRoomId") long chatRoomId, Model model, @LoginUser SessionUser user) {
        User currentUser = userService.findUser(user.getId());
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
        User receiver = chatRoomService.findAnotherUserInChatRoom(chatRoom, currentUser);

        model.addAttribute("receiver", new SessionUser(receiver));
        model.addAttribute("chatRoom", chatRoom);
        return "chat/chatRoom";


    }
}
