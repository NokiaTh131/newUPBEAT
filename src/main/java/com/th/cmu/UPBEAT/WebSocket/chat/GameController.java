package com.th.cmu.UPBEAT.WebSocket.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @MessageMapping("/menu.addUser")
    @SendTo("/topic/menupublic")
    public ChatMessage addUser(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }

    @MessageMapping("/menu.sendMessage")
    @SendTo("/topic/menupublic")
    public ChatMessage sendSignal(ChatMessage message) {
        return message;
    }

}
