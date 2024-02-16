package com.th.cmu.UPBEAT.player;
import com.th.cmu.UPBEAT.Player;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Repository
@Getter
public class PlayerRepository implements PlayerService{

    public Map<String, Player> players = new HashMap<>();
    public List<Player> playersforland = new ArrayList<>();

    @Override
    public Player createNewPlayer(String name) throws IOException {
        if (players.containsKey(name)) return players.get(name);
        Player player = new Player(name,name.hashCode());
        players.put(name, player);
        playersforland.add(player);
        return player;
    }

    @Override
    public ResponseEntity<Player> getPlayerInfo(String name) {
        if (players.containsKey(name)) {
            Player player = players.get(name);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity<Player> editConstruct(String name, String body) {
        if (players.containsKey(name)) {
            Player player = players.get(name);
            player.setConstructionplan(body);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public List<Player> getPlayersforland() {
        return playersforland;
    }
}
