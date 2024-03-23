package com.th.cmu.UPBEAT.game;


import com.th.cmu.UPBEAT.EvalError;
import com.th.cmu.UPBEAT.Parser;
import com.th.cmu.UPBEAT.Player;
import com.th.cmu.UPBEAT.land;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface GameService {
    void startState(int totalPlayers, List<Player> players, land l) throws Parser.SyntaxError, EvalError, IOException;//wait till all player ready and process Turn
    void playerReady();//increase player till ready
    AtomicInteger getReady();
}
