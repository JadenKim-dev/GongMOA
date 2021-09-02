package GongMoa.gongmoa.controller.chat;

import GongMoa.gongmoa.OAuth2.LoginUser;
import GongMoa.gongmoa.OAuth2.SessionUser;
import GongMoa.gongmoa.OAuth2.User;
import GongMoa.gongmoa.domain.chat.ChatMessage;
import GongMoa.gongmoa.domain.chat.ChatRoom;
import GongMoa.gongmoa.domain.chat.ChatRoomJoin;
import GongMoa.gongmoa.domain.form.ChatRoomForm;
import GongMoa.gongmoa.service.UserService;
import GongMoa.gongmoa.service.chat.ChatRoomJoinService;
import GongMoa.gongmoa.service.chat.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {
    private final UserService userService;
    private final ChatRoomJoinService chatRoomJoinService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/chat")
    public String chatRoom(@LoginUser SessionUser user, Model model) {
        model.addAttribute("nickname", user.getName());
        User currentUser = userService.findUser(user.getId());
        log.info("currentUser={}", currentUser.getName());
        List<ChatRoomJoin> chatRoomJoins = chatRoomJoinService.findByUser(currentUser);
        log.info("chatRoomJoins={}", chatRoomJoins.size());
        List<ChatRoomForm> chatRoomForms = chatRoomService.setting(chatRoomJoins, currentUser);
        model.addAttribute("chatRooms", chatRoomForms);
        log.info("chatRooms={}", chatRoomForms.size());

        if(user == null) {
            model.addAttribute("userName", "");
            model.addAttribute("userId", 0);
        } else {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userId", user.getId());
        }
        return "chat/main";
    }

    @PostMapping("/chat/newChat")
    public String newChat(@RequestParam("receiver") String userName1, @RequestParam("sender") String userName2) {
        try{
            long chatRoomId = chatRoomJoinService.newRoom(userName1, userName2);
            return "redirect:/personalChat/" + chatRoomId;
        } catch (NoSuchElementException e) {
            return "redirect:/chat";
        }
    }

    @RequestMapping("/personalChat/{chatRoomId}")
    public String goChat(
            @PathVariable("chatRoomId") long chatRoomId,
            Model model,
            @LoginUser SessionUser user) {
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
        List<ChatMessage> messages = chatRoom.getMessages();
        messages.sort((m1, m2) -> {
            if (m1.getId() > m2.getId()) return -1;
            else return 1;
        });
        List<ChatRoomJoin> chatRoomJoins = chatRoomJoinService.findByChatRoom(chatRoom);

        if(user == null) {
            model.addAttribute("userName", "");
            model.addAttribute("userId", 0);
        } else {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userId", user.getId());
        }

        model.addAttribute("messages", messages);
        model.addAttribute("nickname", user.getName());
        model.addAttribute("chatRoomId", chatRoomId);
        model.addAttribute("profileImg", user.getPicture().getStoreFileName());

        ChatRoomJoin[] result = chatRoomJoins.stream()
                .filter(j -> j.getUser()!=null && !j.getUser().getName().equals(user.getName())).toArray(ChatRoomJoin[]::new);
        if(result.length != 1) {
            return "redirect:/chat";
        } else {
            model.addAttribute("receiver", result[0].getUser());
        }
        return "chat/chatRoom";


    }
}
