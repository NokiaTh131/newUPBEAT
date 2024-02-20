package com.th.cmu.UPBEAT.WebSocket.chat;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessage {
    private String content;
    private String sender;
    private String timeStamp;
    private MessageType type;
}
