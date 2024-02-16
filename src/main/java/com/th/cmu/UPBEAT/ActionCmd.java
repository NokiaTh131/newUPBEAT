package com.th.cmu.UPBEAT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ActionCmd{
    void done() throws ParsingInterruptedException;
    void relocate(land l, Player p) throws ParsingInterruptedException;
    boolean move(Direction direction, Player p , land w) throws ParsingInterruptedException;
    void invest(int money, Player p, land l) throws IOException;
    void collect(int amount, Player player, land l) throws IOException, ParsingInterruptedException;
    void shoot(Direction d, int money, Player p1, land l) throws IOException, ParsingInterruptedException;

    class ParsingInterruptedException extends Throwable {
        public ParsingInterruptedException(String message) {
            super(message);
        }
    }
}

class ActionCommands implements ActionCmd {
    public void done() throws ParsingInterruptedException {
        throw new ParsingInterruptedException("done is invoked");
    }
    public void relocate(land l, Player p) throws ParsingInterruptedException {
        //remove old citycenter
        Cell c = l.getCell(p.getCityCrew().getCurrentRow(), p.getCityCrew().getCurrentCol());
        l.getCell(p.getCenterLoc()[0],p.getCenterLoc()[1]).setCitycenter(false);
        try {
            int x = calculateMinMovingDistance(p.getCenterLoc()[0],p.getCenterLoc()[1],c.getRow(),c.getCol());
            System.out.println("min distance : " + x);
            if(p.getBudget() < 5*x+10 || c.getPlayer_Id() != p.getId()) {
                done();
                return;
            }
            p.setBudget(p.getBudget() - (5*x+10));
            c.setCitycenter(true);
            System.out.println(p.getName() + " city center is " + "(" + p.getCenterLoc()[0] + "," + p.getCenterLoc()[1] + ")");
        }catch (ParsingInterruptedException e) {
            System.out.println("fail to relocate, end turn.");
        }

    }

    public boolean move(Direction direction, Player p , land w) throws ParsingInterruptedException {//don't handle move to edge yet. move should return loc
        p.setBudget(p.getBudget() - 1);
        try{
            if(p.getBudget() < 1) done();
            switch (direction) {
                case UP:
                    if(p.getCityCrew().getCurrentRow() == 0) return false;
                    p.getCityCrew().moveUp();
                    if(p.getCityCrew().getPlayer_Id() != w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id()
                            && w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id() != 0) {
                        p.getCityCrew().moveDown();
                        return true;}
                    break;
                case UPLEFT:
                    if((p.getCityCrew().getCurrentRow() == 0 && p.getCityCrew().getCurrentCol() % 2 == 1) || p.getCityCrew().getCurrentCol() == 0) return false;
                    p.getCityCrew().moveUpLeft();
                    if(p.getCityCrew().getPlayer_Id() != w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id()
                            && w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id() != 0) {
                        p.getCityCrew().moveDownRight();
                        return true;}
                    break;
                case UPRIGHT:
                    if((p.getCityCrew().getCurrentRow() == 0 && p.getCityCrew().getCurrentCol() % 2 == 1) || p.getCityCrew().getCurrentCol() == p.bindings.get("cols") - 1) return false;
                    p.getCityCrew().moveUpRight();
                    if(p.getCityCrew().getPlayer_Id() != w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id()
                            && w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id() != 0) {
                        p.getCityCrew().moveDownLeft();
                        return true;}
                    break;
                case DOWNRIGHT:
                    if((p.getCityCrew().getCurrentRow() == p.bindings.get("rows") - 1 && p.getCityCrew().getCurrentCol() % 2 == 1) || p.getCityCrew().getCurrentCol() == p.bindings.get("cols") - 1) return false;
                    p.getCityCrew().moveDownRight();
                    if(p.getCityCrew().getPlayer_Id() != w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id()
                            && w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id() != 0) {
                        p.getCityCrew().moveUpLeft();
                        return true;}
                    break;
                case DOWN:
                    if(p.getCityCrew().getCurrentRow() == p.bindings.get("rows") - 1) return false;
                    p.getCityCrew().moveDown();
                    if(p.getCityCrew().getPlayer_Id() != w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id()
                            && w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id() != 0) {
                        p.getCityCrew().moveUp();
                        return true;}
                    break;
                case DOWNLEFT:
                    if((p.getCityCrew().getCurrentRow() == p.bindings.get("rows") - 1 && p.getCityCrew().getCurrentCol() % 2 == 1) || p.getCityCrew().getCurrentCol() == 0) return false;
                    p.getCityCrew().moveDownLeft();
                    if(p.getCityCrew().getPlayer_Id() != w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id()
                            && w.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getPlayer_Id() != 0) {
                        p.getCityCrew().moveUpRight();
                        return true;}
                    break;
                default:
                    System.out.println("Invalid direction: " + direction);
            }
        }catch (ParsingInterruptedException e) {
            System.out.println("fail to move, end turn.");
        }

        return false;

    }
    //no-op when invest in not adjacent cell
    public void invest(int money, Player p, land l) throws IOException {
        // The total cost of an investment is i + 1, where i is the investment amount.
        int Cost = 1;
        int investmentCost = money + 1;
        //whatever it will decrease player budget by 1
        p.setBudget(Math.max(p.getBudget() - Cost,0));
        if (p.getBudget() < investmentCost || !isAdjacent(p, l)) {
            return;
        }
        ConfigurationReader r = new ConfigurationReader();

        double newDeposit = Math.min(Math.abs(l.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getDeposit()) + Math.abs(money), (int) r.maxDep());
        l.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).setDeposit(newDeposit);
        //set new player budget
        p.setBudget(Math.max(p.getBudget() - money,0));

        l.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).setP(p);
    }

    public void collect(int amount, Player p, land l) throws IOException, ParsingInterruptedException {
//        System.out.println(player.getName() +" before collect dp " + c.getDeposit());
        // The cost of the collect command is 1 unit
        int Cost = 1;
        p.setBudget(Math.max(p.getBudget() - Cost, 0));
        try{
            if (p.getBudget() < Cost) {
                done();
                return;
            }
            double curdep = l.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getDeposit();
            if (amount > Math.abs((curdep))) {
                // The command acts like a no-op, but the player still pays for the unit cost of the command
                return;
            }

            l.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).setDeposit(Math.abs(curdep) - Math.abs(amount));
            p.setBudget(p.getBudget() + Math.abs(amount));
            if (l.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).getDeposit() <= 0) {
                System.out.println(p.getName() +" lost Cell");
                l.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).setP(new Player());

                if(l.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()).isCitycenter()) {
                    handlePlayerLoss(l.getCell(p.getCityCrew().getCurrentRow(),p.getCityCrew().getCurrentCol()),l);
                }
            }
//            System.out.println(player.getName() +" collect " + amount);
//            System.out.println(player.getName() +" budget " + player.getBudget());
        }catch (ParsingInterruptedException e) {
            System.out.println("fail to collect, end turn.");

        }


    }

    @Override
    public void shoot(Direction d, int money, Player p1, land l) throws IOException, ParsingInterruptedException {
        int shootCost = money + 1;
        if (p1.getBudget() < shootCost) {
            return;//no-op
        }
//        System.out.println(p1.getName() + " have " + p1.getBudget());
        p1.setBudget(p1.getBudget() - shootCost);
        int[] loc = directPeek(d,p1.getCityCrew().getCurrentRow(),p1.getCityCrew().getCurrentCol());
        System.out.println(p1.getName() + " shoot " + money + " at " + l.getCell(loc[0],loc[1]).getRow() + "," + l.getCell(loc[0],loc[1]).getCol());

        // Calculate the new deposit after the attack

        double newDeposit = Math.max(0, l.getCell(loc[0],loc[1]).getDeposit() - money);
        l.getCell(loc[0],loc[1]).setDeposit(newDeposit);
        System.out.println(l.getCell(loc[0],loc[1]).getP().getName() + " left " + l.getCell(loc[0],loc[1]).getDeposit());
        if (newDeposit < 1) {
            if(l.getCell(loc[0],loc[1]).isCitycenter()) {
                handlePlayerLoss(l.getCell(loc[0],loc[1]), l);
            }
            l.getCell(loc[0],loc[1]).setP(new Player());
        }

    }

    public int[] directPeek(Direction d,int currow,int curcol) throws IOException {
        ConfigurationReader c = new ConfigurationReader();
        int[] loc = new int[2];//0 is row and 1 is col
        loc[0] = currow;
        loc[1] = curcol;
        switch (d) {//peekUp
            case UP:
                if(loc[0] > 0) loc[0]--;
                break;
            case UPLEFT:
                if(loc[1] > 0) {
                    if (loc[1] % 2 == 1) {
                        if(loc[0] > 0) {
                            loc[0]--;
                            loc[1]--;
                        }
                        break;
                    }
                    if (loc[1] % 2 == 0) {
                         loc[1]--;
                    }
                }
                break;
            case UPRIGHT:
                if((loc[1] < c.n()-1)) {
                    if (loc[1] % 2 == 1) {
                        if(loc[0] >= 0) {
                            loc[0]--;
                            loc[1]++;
                        }
                        break;
                    }
                    if(loc[1] % 2 == 0){
                        loc[1]++;
                    }
                }
                break;
            case DOWNRIGHT:
                if (loc[1] < c.n()-1) {
                    if (loc[1] % 2 == 1) {
                        loc[1]++;
                        break;
                    }
                    if (loc[1] % 2 == 0) {
                        if(loc[0] < c.m()-1) {
                            loc[0]++;
                            loc[1]++;
                        }
                    }
                }
                break;
            case DOWN:
                if (loc[0] < c.m()-1) loc[0]++;
                break;
            case DOWNLEFT:
                if (loc[1] > 0) {
                    if (loc[1] % 2 == 1) {
                        loc[1]--;
                        break;
                    }
                    if (loc[1] % 2 == 0) {
                        if(loc[0] < c.m()-1) {
                            loc[0]++;
                            loc[1]--;
                        }
                    }
                }
                break;
        }
        return loc;
    }
    public int calculateMinMovingDistance(int startRow, int startCol, int destRow, int destCol) {
        // Convert cube coordinates to axial coordinates
        int startQ = startCol;
        int startR = startRow - (startCol + (startCol & 1)) / 2;

        int destQ = destCol;
        int destR = destRow - (destCol + (destCol & 1)) / 2;

        // Calculate axial coordinates distance
        int dQ = Math.abs(destQ - startQ);
        int dR = Math.abs(destR - startR);
        int dS = Math.abs(-destQ - destR + startQ + startR);

        // Return the maximum coordinate difference as the minimum moving distance
        return Math.max(dQ, Math.max(dR, dS));
    }

    private void handlePlayerLoss(Cell target, land l) throws IOException, ParsingInterruptedException {
        System.out.println(target.getP().getName() + " has lost the game!");
        Player lp = target.getP();
//        l.players.remove(target.getP());
        target.setCitycenter(false);
        for(int i = 0;i < l.map.row;i++) {
            for(int j = 0;j < l.map.col;j++) {
                if(lp.getId() == (l.getCell(i, j).getP().getId())) {
                    double oldDeposit = l.getCell(i,j).getDeposit();
                    l.getCell(i,j).setP(new Player());
                    l.getCell(i,j).setDeposit(oldDeposit);
                }
            }
        }
        done();
    }

    private boolean isAdjacent(Player p, land l) throws IOException {
        int row = p.getCityCrew().getCurrentRow();
        int col = p.getCityCrew().getCurrentCol();
        for (Direction direction : Direction.values()) {
            int[] loc = directPeek(direction,row,col);
            if(l.getCell(loc[0],loc[1]).getPlayer_Id() == p.getId()) return true;
        }
        return false;
    }

}
