package com.th.cmu.UPBEAT;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConstructionPlanReader {
    /*read file and build one line string*/
    public String read(String filename) throws IOException {
        StringBuilder constructionPlanCode = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                int indexOfHash = line.indexOf('#');
                if (indexOfHash != -1) {
                    line = line.substring(0, indexOfHash);
                }
                constructionPlanCode.append(line.trim()).append(" ");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
        return constructionPlanCode.toString();
    }

    /*read file and build separate lines string*/
    public String readPretty(String filename) throws IOException {
        StringBuilder constructionPlanCode = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                constructionPlanCode.append(line.trim()).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
        return constructionPlanCode.toString();
    }

    public static void main(String[] args) throws IOException {
        ConstructionPlanReader c = new ConstructionPlanReader();
        System.out.println(c.readPretty("src/main/java/com/th/cmu/UPBEAT/constructionplan2.txt"));
    }
}
