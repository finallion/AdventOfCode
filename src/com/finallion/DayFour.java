package com.finallion;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DayFour implements Day {

    @Override
    public void partOne() {
        System.out.println("The result is: " + readFile());
    }

    @Override
    public void partTwo() {
        System.out.println("The result is: " + readFile());
    }

    public int readFile() {
        File file = new File(this.buildPath("Four"));
        int counter = 0;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] pair = scanner.nextLine().split(",");

                int startOne = Integer.parseInt(pair[0].split("-")[0]);
                int endOne = Integer.parseInt(pair[0].split("-")[1]);
                int startTwo = Integer.parseInt(pair[1].split("-")[0]);
                int endTwo = Integer.parseInt(pair[1].split("-")[1]);

                if (startOne <= startTwo) {
                    if (endOne >= endTwo) {
                        counter++;
                    } else if (startTwo <= endOne) {
                        counter++;
                    }
                } else if (startTwo <= startOne) {
                    if (endTwo >= endOne) {
                        counter++;
                    } else if (startOne <= endTwo) {
                        counter++;
                    }
                }


                // part one
                /*
                if (startOne <= startTwo && endOne >= endTwo) {
                    counter++;
                } else if (startTwo <= startOne && endTwo >= endOne) {
                    counter++;
                }
                 */

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counter;
    }
}
