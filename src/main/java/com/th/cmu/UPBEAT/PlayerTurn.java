package com.th.cmu.UPBEAT;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class PlayerTurn {
    private static final Scanner scanner = new Scanner(System.in);
    static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws IOException, Parser.SyntaxError, EvalError {
        PlayerTurn pt = new PlayerTurn();
        // Assume p1 and p2 are your Player instances
        List<Player> players = new ArrayList<>();
        Player p1 = new Player("John",1);
        Player p2 = new Player("Jesse",2);
        Player p3 = new Player("Josh",3);
        players.add(p1);players.add(p2);players.add(p3);
        land l = new land(players);
        l.buyCity(p1);
        l.buyCity(p2);
        l.buyCity(p3);
        l.printMatrix();

        List<Player> playersIngame = new ArrayList<>(players);
        Iterator<Player> itr;

        while (playersIngame.size() > 1) {
            itr = playersIngame.iterator();
            while (itr.hasNext()) {
                Player p = itr.next();
                if (!p.isDead) {
                    pt.askPlayerForConstructionPlan(p, l);
                } else {
                    System.out.println(p.getName() + " is dead GG.");
                    itr.remove(); // Remove dead player from the list
                }
                l.printMatrix();
            }
        }
        System.out.println("winner is " + players.getFirst().getName());
        // Shut down the executor service when done
        executorService.shutdown();
    }

    public static void TurnRun(List<Player> players,PlayerTurn pt,land l) throws Parser.SyntaxError, EvalError, IOException {
        List<Player> playersIngame = new ArrayList<>(players);
        Iterator<Player> itr;
        while (playersIngame.size() > 1) {
            itr = playersIngame.iterator();
            while (itr.hasNext()) {
                Player p = itr.next();
                if (!p.isDead) {
                    pt.askPlayerForConstructionPlan(p, l);
                } else {
                    System.out.println(p.getName() + " is dead GG.");
                    itr.remove(); // Remove dead player from the list
                }
                l.printMatrix();
            }
        }
    }




    public void askPlayerForConstructionPlan(Player player, land l) throws Parser.SyntaxError, EvalError, IOException {
        ConfigurationReader c = new ConfigurationReader();
        Future<Boolean> future = executorService.submit(() -> {
            try {
                String answer = scanner.nextLine().trim().toLowerCase();
                return answer.equals("yes");
            } catch (Exception e) {
                return false; // Handle exceptions, e.g., if the user enters an invalid input
            }
        });

        try {
            System.out.println(player.getName() + ", do you want to change your construction plan? (yes/no)");
            boolean changePlan = future.get((c.initPlanMin()*60) + c.initPlanSec(), TimeUnit.SECONDS); // Wait for up to 10 seconds
            if (changePlan) {
                isFileModified(player.filePath);//is file has modified in def time.return true if modified
                executePlayerTurn(player,l, player.filePath);
                System.out.println(player.getName() + ", you chose to change your construction plan.");
            } else {
                System.out.println(player.getName() + ", you chose not to change your construction plan.");
                executePlayerTurn(player,l, player.filePath);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println(player.getName() + ", time is up. Defaulting to the current construction plan.");
            executePlayerTurn(player,l, player.filePath);
        } catch (IOException | Parser.SyntaxError | EvalError e) {
            throw new RuntimeException(e);
        }
    }



    private static boolean isFileModified(String filename) throws IOException, InterruptedException {
        ConstructionPlanReader c = new ConstructionPlanReader();
        ConfigurationReader cf = new ConfigurationReader();
        String filePath = filename;
        String old = c.read(filePath);
        String newf = c.read(filePath);
        openConstructionPlanFile(filename);
        int time = 0;
        int timeout = (int) ((cf.planRevMin() * 60) + cf.planRevSec());
        while(old.equals(newf) && time != timeout) {
            newf = c.read(filePath);
            Thread.sleep(1000);//wait 1 second
            time++;
            System.out.println(time);
        }
        if(time >= timeout) {
            System.out.println("file not modified");
            return false;
        }
        System.out.println("file has been modified");
        return true;
    }



    private static void openConstructionPlanFile(String filename) {
        try {
            // Replace "constructionplan.txt" with the actual path to your construction plan file
            File file = new File(filename);

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().edit(file);
            } else {
                System.out.println("Desktop open action is not supported on this platform.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while opening the construction plan file.");
        }
    }



    public void executePlayerTurn(Player player, land l, String constructfile) throws IOException, Parser.SyntaxError, EvalError {
        ConstructionPlanReader constructionPlanReader = new ConstructionPlanReader();
        String constructionPlan = constructionPlanReader.read(constructfile);

        ExprTokenizer tokenizer = new ExprTokenizer("t = t + 1 " + constructionPlan);

        ExprParser parser = new ExprParser(tokenizer,l,player);
        List<Expr> result = parser.parsePlan();
        try {
            player.setCityCrewLoc(player.getCenterLoc()[0], player.getCenterLoc()[1]);
            for (Expr statement : result) {
                statement.eval(player.bindings);
            }
        } catch (ActionCmd.ParsingInterruptedException e) {
            System.out.println(player.getName() + " budget " + player.getBudget());
            l.printOwner(player);
            System.out.println(player.getName() + " " + player.bindings.get("t") + " turn end.");
        } catch (EvalError | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
