package com.th.cmu.UPBEAT;

import java.io.IOException;
import java.util.List;
import java.util.Random;


public interface Territory {
    void buyCity(Player p) throws IOException;
    Cell getCell(int row, int col);
}

