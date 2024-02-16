package com.th.cmu.UPBEAT.player;
import com.th.cmu.UPBEAT.Player;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface PlayerService {
    public Player createNewPlayer(String name) throws IOException;

    ResponseEntity<Player> getPlayerInfo(String name);

    ResponseEntity<Player> editConstruct(String name, String body);

    List<Player> getPlayersforland();

}
