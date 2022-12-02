package com.finallion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DayOne implements Day {

    @Override
    public void partOne() {
        List<Integer> calories = readFile();
        calories.sort(Collections.reverseOrder());
        System.out.println("The max calorie count is: " + calories.get(0));
    }

    @Override
    public void partTwo() {
        List<Integer> calories = readFile();
        calories.sort(Collections.reverseOrder());
        int sumOfFirstThree = calories.get(0) + calories.get(1) + calories.get(2);
        System.out.println("The max calorie count is: " + sumOfFirstThree);
    }

    public List<Integer> readFile() {
        File file = new File(this.buildPath("One"));
        List<Integer> caloriesPerElf = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            int index = 0;
            int currentCalories = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    caloriesPerElf.add(index, currentCalories);
                    index++;
                    currentCalories = 0;
                } else {
                    currentCalories += Integer.parseInt(line);
                }
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return caloriesPerElf;
    }
}
