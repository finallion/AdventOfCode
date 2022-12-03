package com.finallion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayTwo implements Day {

    @Override
    public void partOne() {
        System.out.println("The result is: " + readFile());
    }

    @Override
    public void partTwo() {
        System.out.println("The result is: " + readFile());
    }

    public int readFile() {
        File file = new File(this.buildPath("Two"));
        int score = 0;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // part two
                switch (line) {
                    case "A X" -> score += 0 + 3;
                    case "A Y" -> score += 3 + 1;
                    case "A Z" -> score += 6 + 2;
                    case "B X" -> score += 0 + 1;
                    case "B Y" -> score += 3 + 2;
                    case "B Z" -> score += 6 + 3;
                    case "C X" -> score += 0 + 2;
                    case "C Y" -> score += 3 + 3;
                    case "C Z" -> score += 6 + 1;
                }

                // part one
                /*
                switch (line) {
                    case "A X" -> score += 3 + 1;
                    case "A Y" -> score += 6 + 2;
                    case "A Z" -> score += 0 + 3;
                    case "B X" -> score += 0 + 1;
                    case "B Y" -> score += 3 + 2;
                    case "B Z" -> score += 6 + 3;
                    case "C X" -> score += 6 + 1;
                    case "C Y" -> score += 0 + 2;
                    case "C Z" -> score += 3 + 3;
                }
                 */
            }

            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return score;
    }
}
