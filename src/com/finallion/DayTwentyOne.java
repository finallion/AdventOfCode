package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayTwentyOne implements Day {
    long numberToYell = 0;

    @Override
    public void partOne() {
        List<Monkey> input = readFile(true);

        Monkey root = input.stream().filter(monkey -> monkey.name.equals("root")).findFirst().get();
        long result = cycle(root, input);
    }

    @Override
    public void partTwo() {
        List<Monkey> input = readFile(true);

        Monkey root = input.stream().filter(monkey -> monkey.name.equals("root")).findFirst().get();
        long number = cyclePartTwo(root, input);

        while (number != 0) {
            number = cyclePartTwo(root, input);
        }

        System.out.println(numberToYell);
    }

    private long cyclePartTwo(Monkey root, List<Monkey> input) {
        if (!root.yellNumber) {
            char sign = root.operation.split(" ")[1].charAt(0);

            if (root.name.equals("root")) {
                sign = '=';
            }

            String nameMonkeyOne = root.operation.split(" [+\\-*/] ")[0];
            Monkey monkeyOne = input.stream().filter(monkey -> monkey.name.equals(nameMonkeyOne)).findFirst().get();
            String nameMonkeyTwo = root.operation.split(" [+\\-*/] ")[1];
            Monkey monkeyTwo = input.stream().filter(monkey -> monkey.name.equals(nameMonkeyTwo)).findFirst().get();

            switch (sign) {
                case '+' -> {
                    return cyclePartTwo(monkeyOne, input) + cyclePartTwo(monkeyTwo, input);
                }
                case '-' -> {
                    return cyclePartTwo(monkeyOne, input) - cyclePartTwo(monkeyTwo, input);
                }
                case '*' -> {
                    return cyclePartTwo(monkeyOne, input) * cyclePartTwo(monkeyTwo, input);
                }
                case '/' -> {
                    return cyclePartTwo(monkeyOne, input) / cyclePartTwo(monkeyTwo, input);
                }
                default -> {
                    long resultOne = cyclePartTwo(monkeyOne, input);
                    long resultTwo = cyclePartTwo(monkeyTwo, input);
                    if (resultOne == resultTwo) {
                        return 0;
                    } else {
                        // monkey two is constant.
                        // TODO: implement binary search, this was solved with an online calculator.
                        numberToYell++;
                        return -1;
                    }
                }
            }
        } else {
            if (root.name.equals("humn")) {
                return numberToYell;
            }
            return Long.parseLong(root.operation);
        }
    }

    private long cycle(Monkey root, List<Monkey> input) {
        if (!root.yellNumber) {
            char sign = root.operation.split(" ")[1].charAt(0);

            String nameMonkeyOne = root.operation.split(" [+\\-*/] ")[0];
            Monkey monkeyOne = input.stream().filter(monkey -> monkey.name.equals(nameMonkeyOne)).findFirst().get();
            String nameMonkeyTwo = root.operation.split(" [+\\-*/] ")[1];
            Monkey monkeyTwo = input.stream().filter(monkey -> monkey.name.equals(nameMonkeyTwo)).findFirst().get();

            switch (sign) {
                default -> {
                    return cycle(monkeyOne, input) + cycle(monkeyTwo, input);
                }
                case '-' -> {
                    return cycle(monkeyOne, input) - cycle(monkeyTwo, input);
                }
                case '*' -> {
                    return cycle(monkeyOne, input) * cycle(monkeyTwo, input);
                }
                case '/' -> {
                    return cycle(monkeyOne, input) / cycle(monkeyTwo, input);
                }
            }
        } else {
            return Long.parseLong(root.operation);
        }
    }




    public List<Monkey> readFile(boolean partOne) {
        List<Monkey> input = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");

        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("TwentyOne")));
            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                String name = line.split(":")[0];
                if (matcher.find()) {
                    input.add(new Monkey(name, true, matcher.group()));
                } else {
                    input.add(new Monkey(name, false, line.split(": ")[1]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    private record Monkey(String name, boolean yellNumber, String operation) {}

}

