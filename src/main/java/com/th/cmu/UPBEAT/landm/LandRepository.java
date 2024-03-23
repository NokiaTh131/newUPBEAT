package com.th.cmu.UPBEAT.landm;

import com.th.cmu.UPBEAT.*;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin
@Repository
public class LandRepository implements LandService{

    public Map<List<Player>, land> lands = new HashMap<>();


    @Override
    public land createNewLand(List<Player> players) throws IOException {
        if (lands.containsKey(players)) {
            return lands.get(players);
        }
        land l = new land(players);
        for (Player p :players) {
            l.buyCity(p);
        }
        lands.put(players, l);
        return l;
    }
    @Override
    public ResponseEntity<land> getLandInfo(List<Player> players) throws IOException {
        if(lands.containsKey(players)){
            return new ResponseEntity<>(lands.get(players), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void parse(ResponseEntity<Player> playerInfo, List<Player> players) throws Parser.SyntaxError, EvalError, IOException, ActionCmd.ParsingInterruptedException {
        PlayerTurn pt = new PlayerTurn();
        pt.executePlayerTurn(playerInfo.getBody(),lands.get(players), Objects.requireNonNull(playerInfo.getBody()).filePath);
    }
}
