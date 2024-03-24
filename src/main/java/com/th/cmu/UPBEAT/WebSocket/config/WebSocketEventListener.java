package com.th.cmu.UPBEAT.WebSocket.config;


import com.th.cmu.UPBEAT.WebSocket.chat.ChatMessage;
import com.th.cmu.UPBEAT.WebSocket.chat.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

//    private final SimpMessageSendingOperations messagingTemplete;
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        SimpMessageHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
//        var chatMessage = ChatMessage.builder().sender(username).type(MessageType.LEAVE).build();
//        messagingTemplete.convertAndSend("/topic/menupublic",chatMessage);
//    }
}
