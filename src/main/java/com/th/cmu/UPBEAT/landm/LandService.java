package com.th.cmu.UPBEAT.landm;

import com.th.cmu.UPBEAT.EvalError;
import com.th.cmu.UPBEAT.Parser;
import com.th.cmu.UPBEAT.Player;
import com.th.cmu.UPBEAT.land;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface LandService {
    land createNewLand(List<Player> players) throws IOException;

    ResponseEntity<land> getLandInfo(List<Player> playersforland) throws IOException;

    void parse(ResponseEntity<Player> playerInfo, List<Player> players) throws Parser.SyntaxError, EvalError, IOException;
}
