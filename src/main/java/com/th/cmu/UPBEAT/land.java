package com.th.cmu.UPBEAT;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
public class land implements Territory {
    ConfigurationReader c;
    public WeightedGraph map;
    public List<Player> players;
    private Set<String> selectedPlaces = new HashSet<>();
    public int plan_rev_min = 0;
    public int plan_rev_sec= 0;
    public int init_plan_min = 0;
    public int init_plan_sec = 0;

    public land(List<Player> players) throws IOException {
        c = new ConfigurationReader();
        this.map = new WeightedGraph(c.m(), c.n());
        this.players = players;
        plan_rev_min = (int)c.planRevMin();
        plan_rev_sec= (int) c.planRevSec();
        init_plan_min = (int) c.initPlanMin();
        init_plan_sec = (int) c.initPlanSec();
    }

    //spec : buycity is invoked only first time game is started for set player initial random cell
    @Override
    public void buyCity(Player p) throws IOException {
        Random random = new Random();
        int maxRows = (int) c.m();
        int maxCols = (int) c.n();

        // Keep looping until we find a unique place
        while (true) {
            int row = random.nextInt(maxRows);
            int col = random.nextInt(maxCols);

            String placeKey = row + "," + col;

            // If this place is already selected, try again
            if (selectedPlaces.contains(placeKey)) {
                continue;
            }

            // Otherwise, mark this place as selected and break the loop
            selectedPlaces.add(placeKey);
            map.addEdge(row, col, p, (int) c.initCenterDep());
            p.setCityCenter(this.getCell(row, col));
            p.setCityCrewLoc(row, col);
            break;
        }
    }

    @Deprecated
    public void buyCity(int row, int col, Player p) throws IOException {
        map.addEdge(row, col, p, (int) c.initCenterDep());
        p.setCityCenter(this.getCell(row, col));
        p.setCityCrewLoc(row, col);
    }

    public void printMatrix() {
        map.printGraphMatrix();
    }

    public Cell getCell(int row, int col) {
        return map.getCell(row, col);
    }

    public void printOwner(Player p) {
        map.printOwner(p);
    }

    public int getIndexOfPlayer(Player p, List<Player> playerList) {
        return playerList.indexOf(p);
    }

}
