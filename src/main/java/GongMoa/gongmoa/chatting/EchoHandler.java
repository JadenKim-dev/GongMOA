package GongMoa.gongmoa.chatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class EchoHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessionList = new ArrayList<>();

//    Map<String,WebSocketSession> us ers = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        String username = searchUserName(session);
////        for(WebSocketSession sess : sessionList) {
////            sess.sendMessage(new TextMessage(user_name+"님이 접속했습니다."));
////        }
        sessionList.add(session);
        log.info("접속 : {}",  session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String username = searchUserName(session);

        log.info("메세지 전송 = {} : {}", session, message.getPayload());

//        //사용자가 접속중인지 아닌지
//        WebSocketSession chatwritingSession =users.get("username");
//        if(chatwritingSession != null) {
//            TextMessage textMessage = new TextMessage(username+ " 님이 메세지를 보냈습니다.");
//            chatwritingSession.sendMessage(textMessage);
//        }
        for(WebSocketSession sess: sessionList) {
            sess.sendMessage(new TextMessage(message.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        String user_name = searchUserName(session);
//        for(WebSocketSession sess : sessionList) {
//            sess.sendMessage(new TextMessage(user_name + "님의 연결이 끊어졌습니다."));
//        }
        sessionList.remove(session);
        log.info("퇴장 : {}",  session);
    }

    public String searchUserName(WebSocketSession session) throws Exception {
        String username;
        Map<String, Object> map;
        map = session.getAttributes();
        username = (String) map.get("username");
        return username;
    }
}

