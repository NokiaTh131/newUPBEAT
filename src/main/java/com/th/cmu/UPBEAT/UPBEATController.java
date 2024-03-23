package com.th.cmu.UPBEAT;

import com.th.cmu.UPBEAT.game.GameService;
import com.th.cmu.UPBEAT.landm.LandService;
import com.th.cmu.UPBEAT.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@CrossOrigin
@RestController
public class UPBEATController {
    @Autowired
    private PlayerService playerservice;
    @Autowired
    private LandService landservice;
    @Autowired
    private GameService gameService;

    @PostMapping("/player")
    public Player createPlayer(@RequestBody String body) throws IOException {//create player in server
        return this.playerservice.createNewPlayer(body);
    }

    @GetMapping("/player/{name}")
    public ResponseEntity<Player> getPlayer(@PathVariable("name") String name) {
        return this.playerservice.getPlayerInfo(name);
    }

    @PutMapping("/player/{name}/constructionplan")
    public ResponseEntity<Player> editConstructionplan(@PathVariable("name") String name, @RequestBody String body) {
        return this.playerservice.editConstruct(name, body);
    }
    @GetMapping("/land")
    public ResponseEntity<land> getLand() throws IOException {
        return this.landservice.getLandInfo(playerservice.getPlayersforland());
    }
    @PostMapping("/land")
    public land createLand() throws IOException, ActionCmd.ParsingInterruptedException, Parser.SyntaxError, EvalError {//create player in server
        return this.landservice.createNewLand(playerservice.getPlayersforland());
    }

    @PostMapping("/player/{name}/parse")
    public void invokeParse(@PathVariable("name") String name) throws Parser.SyntaxError, EvalError, IOException, ActionCmd.ParsingInterruptedException {
        this.landservice.parse(playerservice.getPlayerInfo(name),playerservice.getPlayersforland());
    }

    @PostMapping("/newGame")
    public void newGame() throws Parser.SyntaxError, EvalError, IOException, ActionCmd.ParsingInterruptedException, InterruptedException {
        this.gameService.startState(playerservice.getPlayersforland().size(),playerservice.getPlayersforland(),landservice.getLandInfo(playerservice.getPlayersforland()).getBody());
    }

    @PutMapping("/newGame")
    public void playerReady() {
        this.gameService.playerReady();
    }

    @GetMapping("/newGame")
    public AtomicInteger getPlayerReady() {
        return this.gameService.getReady();
    }



}
