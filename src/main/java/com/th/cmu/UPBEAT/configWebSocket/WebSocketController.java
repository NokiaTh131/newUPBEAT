package com.th.cmu.UPBEAT.configWebSocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/update-data")
    @SendTo("/topic/data-updated")
    public String updateData() {
        return "Data updated";
    }
}