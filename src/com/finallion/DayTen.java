package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DayTen implements Day {
    private int x = 1;
    private int cycle = 1;
    private int sum = 0;
    private char[][] crt = new char[6][40];
    private int row = 0;


    @Override
    public void partOne() {
        //readFile();
        //System.out.println(sum);
    }

    @Override
    public void partTwo() {
        readFile();

        for (int i = 0; i < crt.length; i++) {
            for (int ii = 0; ii < crt[0].length; ii++) {
                System.out.print(crt[i][ii] + " ");
            }
            System.out.println();
        }
    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Ten")));

            for (int i = 0; i < lines.size(); i++) {
                if (!lines.get(i).equals("noop")) {
                    String[] parts = lines.get(i).split(" ");
                    cycle++;
                    checkCyclePartTwo(); // change for part one
                    x += Integer.parseInt(parts[1]);
                }
                cycle++;
                checkCyclePartTwo(); // change for part one
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkCyclePartTwo() {
        if (cycle == x || cycle == x + 1 || cycle == x + 2) {
            crt[row][cycle] = '#';
        }

        if (cycle % 40 == 0) {
            row++;
            cycle -= 40;
        }
    }

    private void checkCycle() {
        // part one
        if (cycle % 40 == 20) {
            sum += cycle * x;
        }
    }
}
