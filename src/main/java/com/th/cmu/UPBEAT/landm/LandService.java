package com.th.cmu.UPBEAT.landm;

import com.th.cmu.UPBEAT.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface LandService {
    land createNewLand(List<Player> players) throws IOException;
    ResponseEntity<land> getLandInfo(List<Player> playersforland) throws IOException;
    void parse(ResponseEntity<Player> playerInfo, List<Player> players) throws Parser.SyntaxError, EvalError, IOException, ActionCmd.ParsingInterruptedException;
}
