package com.th.cmu.UPBEAT;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Player {
    public boolean isDead;
    private String name;
    private int id;
    private int citycenRow = -1;
    private int citycenCol = -1;
    private CityCrew cityCrew;
    public Map<String, Integer> bindings = new HashMap<>();
    public String filePath;

    public Player(String name, int id) throws IOException {
        ConfigurationReader r = new ConfigurationReader();
        bindings.put("rows", (int)r.m());
        bindings.put("cols", (int)r.n());
        this.name = name;
        this.id = id;
        this.isDead = false;
        bindings.put("budget",(int) r.initBudget());
        this.cityCrew = new CityCrew(this);
        bindings.put("t", 0);
        bindings.put("deposit",(int) r.initCenterDep());
        bindings.put("m", 0);
        bindings.put("opponentLoc", 0);
        bindings.put("cost", 0);
        bindings.put("dir", 0);
        bindings.put("random",0 );
        bindings.put("int",10000);
        bindings.put("interest_pct",(int)r.interestPct());
        bindings.put("max_dep",(int)r.maxDep());
        bindings.put("currow", getCenterLoc()[0]);
        bindings.put("curcol", getCenterLoc()[1]);
        this.filePath = "src/main/java/com/th/cmu/UPBEAT/constructionplans/" + name + id + ".txt";
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public Player() throws IOException {
        this("N_A",0);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void SetCenterLoc(long row, long col) {
        this.citycenRow = (int)row;
        this.citycenCol = (int)col;
    }

    public int[] getCenterLoc() {
        int[] Loc = new int[2];
        Loc[0] = this.citycenRow;
        Loc[1] = this.citycenCol;
        return Loc;
    }

    public int getBudget() {
        return bindings.get("budget");
    }

    public void setBudget(int budget) {
        bindings.put("budget",budget);
    }

    public CityCrew getCityCrew() {
        return cityCrew;
    }

    public void setCityCenter(Cell c) {
        c.setCitycenter(true);
    }

    public void setCityCrewLoc(int row,int col) {
        bindings.put("currow", row);
        bindings.put("curcol", col);
    }

    public String getConstructionplan() throws IOException {
        ConstructionPlanReader c = new ConstructionPlanReader();
        return c.readPretty(filePath);
    }

    public void setConstructionplan(String constructionplan) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(constructionplan);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}

