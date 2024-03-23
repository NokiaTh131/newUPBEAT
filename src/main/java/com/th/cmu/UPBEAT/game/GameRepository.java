package com.th.cmu.UPBEAT.game;


import com.th.cmu.UPBEAT.*;
import com.th.cmu.UPBEAT.WebSocket.chat.ChatMessage;
import com.th.cmu.UPBEAT.WebSocket.chat.GameController;
import com.th.cmu.UPBEAT.WebSocket.chat.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@CrossOrigin
@Repository
public class GameRepository implements GameService{

    private final AtomicInteger playersReady = new AtomicInteger(0);; // Number of players who are ready
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameRepository(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @Override
    public void startState(int totalPlayers, List<Player> players, land l) throws Parser.SyntaxError, EvalError, IOException {
        // This method will wait until all players are ready
        while (playersReady.get() < totalPlayers) {
            // Wait until all players send their messages
            try {
                Thread.sleep(1000); // You can adjust the sleep time as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for players to be ready", e);
            }
        }
        TurnRun(players, l);
    }

    @Override
    public AtomicInteger getReady() {
        return playersReady;
    }

    // Method to be called when a player sends a message indicating they are ready
    @Override
    public void playerReady() {
        playersReady.incrementAndGet(); // Increment the count of ready players
    }

    public synchronized void TurnRun(List<Player> players, land l) throws Parser.SyntaxError, EvalError, IOException {
        List<Player> playersIngame = new ArrayList<>(players);
        Iterator<Player> itr;
        while (playersIngame.size() > 1) {
            itr = playersIngame.iterator();
            while (itr.hasNext()) {
                Player p = itr.next();
                int tmp = p.bindings.get("t");
                System.out.println(p.getName());
                if (!p.isDead) {
                    String username = p.getName();
                    var chatMessage = ChatMessage.builder().sender(username).content(username).type(MessageType.CHAT).build();
                    messagingTemplate.convertAndSend("/topic/menupublic",chatMessage);
                    System.out.println(chatMessage.getContent() + " turn");

                    while(tmp == p.bindings.get("t")) {

                    }

                } else {
                    System.out.println(p.getName() + " is dead GG.");
                    itr.remove(); // Remove dead player from the list
                }

            }
        }
    }
}
