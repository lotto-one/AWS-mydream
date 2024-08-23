package kr.ict.mydream.chat;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.net.SyslogAppender;
import kr.ict.mydream.vo.ChatDetailVO;
import kr.ict.mydream.vo.ChatRoomVO;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketChatHandler extends TextWebSocketHandler {
    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> SESSION_ROOMS = new ConcurrentHashMap<>();

    @Autowired
    private ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        System.out.println("Client connected: " + session.getId());

        // 세션 속성에서 프로토콜 값 가져오기
        String protocol = (String) session.getAttributes().get("protocol");
        if (protocol != null) {
            System.out.println("Received protocol in afterConnectionEstablished: " +
                    protocol);
        }
        System.out.println(protocol);
        String[] inchat = protocol.split("-");
        System.out.println(inchat.length);

        ChatRoomVO chatroomvo = new ChatRoomVO();
        try {
            if (inchat.length == 2) {
                System.out.println("와써요!");
                chatroomvo.setMemno(Integer.parseInt(inchat[0]));
                chatroomvo.setCnsno(Integer.parseInt(inchat[1]));
                // String chtroom = String.valueOf(chatService.getchatcroom(chatroomvo));
                // System.out.println(chtroom);
                SESSION_ROOMS.put(session.getId(), String.valueOf(inchat[1]));
                CLIENTS.put(session.getId(), session);
                sendMessageToClient(session, String.valueOf(inchat[1]));

            } else if (inchat.length == 1) {
                int intprotocol = Integer.parseInt(inchat[0]);
                int roomnum = chatService.chackRoom(intprotocol);
                String chtroom = String.valueOf(roomnum);
                System.out.println(chtroom);
                System.out.println("연결되었습니다.");
                // 채팅방 설정 및 사용자 설정
                SESSION_ROOMS.put(session.getId(), chtroom);
                CLIENTS.put(session.getId(), session);
                // 값 뷰로 전달
                sendMessageToClient(session, chtroom);

            } else {
                System.out.println("잘못된 요청");
            }
        } catch (Exception e) {
            System.out.println("연결 오류");
        }

    }

    private void sendMessageToClient(WebSocketSession session, String message) {
        try {
            TextMessage textMessage = new TextMessage(message);
            session.sendMessage(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("연결이 끊겼습니다.");
        SESSION_ROOMS.remove(session.getId());
        CLIENTS.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = session.getId();
        String room = SESSION_ROOMS.get(sessionId);

        System.out.println("Message from " + sessionId + " in room " + room + ": " + message.getPayload());
        String payload = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(message);
        System.out.println(payload + "------------------------------------------------------");
        // JSON 문자열을 Map 객체로 변환
        Map<String, Object> messageMap = objectMapper.readValue(payload, Map.class);
        // String chtnostr = (String) (messageMap.get("chtno"));
        int chtno = Integer.parseInt((String) (messageMap.get("chtno")));
        String content = (String) messageMap.get("content");
        String chatdiv = (String) messageMap.get("chatdiv");
        System.out.println("방 : " + chtno + ",발송자 : " + chatdiv + ",내용:" + content);
        ChatDetailVO vo = new ChatDetailVO();
        vo.setChtno(chtno);
        vo.setChatdiv(chatdiv);
        vo.setContent(content);
        chatService.chatSave(vo);
        // vo 객체를 JSON 문자열로 형태로 변환
        String voJson = objectMapper.writeValueAsString(vo);
        // 클라이언트로 vo JSON 문자열을 전송
        TextMessage voMessage = new TextMessage(voJson);
        System.out.println("--------------------");
        CLIENTS.entrySet().stream()
                .filter(entry -> SESSION_ROOMS.get(entry.getKey()).equals(room) && !entry.getKey().equals(sessionId))
                .forEach(entry -> {
                    try {
                        entry.getValue().sendMessage(voMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
