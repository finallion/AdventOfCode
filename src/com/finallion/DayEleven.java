package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DayEleven implements Day {
    private List<Monkey> monkeys = new ArrayList<>();
    private int number = 0;
    private List<Integer> dividers = new ArrayList<>();

    @Override
    public void partOne() {
        /*
        readFile();
        for (int row = 0; row < 20; row++) {
            for (int ii = 0; ii < monkeys.size(); ii++) {
                Monkey monkey = monkeys.get(ii);
                while (!monkey.items.isEmpty()) {
                    long item = monkey.items.poll();
                    monkey.inspectedItems++;
                    item = monkey.addMonkeyOperation(item);
                    item = monkey.addBoredom(item);
                    monkeys.get(monkey.getNextMonkey(item)).addItem(item);
                }
            }
        }

        monkeys.sort((k, v) -> Integer.compare(v.inspectedItems, k.inspectedItems));
        System.out.println("The result is: " + (monkeys.get(0).inspectedItems * monkeys.get(1).inspectedItems));

         */
    }

    @Override
    public void partTwo() {
        readFile();
        for (int i = 0; i < 10000; i++) {
            for (int ii = 0; ii < monkeys.size(); ii++) {
                Monkey monkey = monkeys.get(ii);
                while (!monkey.items.isEmpty()) {
                    long item = monkey.items.poll();
                    monkey.inspectedItems++;
                    item = monkey.addMonkeyOperation(item);
                    item = item % getLeastCommonMultiple(); // reduce item size by taking the remainder of lcm
                    monkeys.get((int)monkey.getNextMonkey(item)).addItem(item);
                }
            }
        }

        monkeys.sort((k, v) -> Integer.compare(v.inspectedItems, k.inspectedItems));
        System.out.println("The result is: " + ((long) monkeys.get(0).inspectedItems * monkeys.get(1).inspectedItems));
    }

    // all are prime numbers, so it can just be multiplied, no need for division method
    private int getLeastCommonMultiple() {
        int lcm = 1;
        for (Integer i : dividers) {
            lcm *= i;
        }
        return lcm;
    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Eleven")));

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("Monkey")) {
                    String items = lines.get(i + 1);
                    String[] worryLevels = items.split(": ")[1].split(", ");
                    String[] operation = lines.get(i + 2).split("old ")[1].split(" ");

                    Deque<Long> levels = new ArrayDeque<>();
                    for (String worryLevel : worryLevels) {
                        levels.addLast(Long.parseLong(worryLevel));
                    }

                    //part two
                    int divider = Integer.parseInt(lines.get(i + 3).split("by ")[1]);
                    dividers.add(divider);

                    monkeys.add(new Monkey(levels, number++, operation[0], operation[1],
                            divider,
                            Integer.parseInt(lines.get(i + 4).split("monkey ")[1]),
                            Integer.parseInt(lines.get(i + 5).split("monkey ")[1])));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class Monkey {
        private Deque<Long> items;
        private int number;
        private String operator;
        private String operation;
        private int testCase;
        private int monkeyTrue;
        private int monkeyFalse;
        private int inspectedItems;

        public Monkey(Deque<Long> items, int number, String operator, String operation, int testCase, int monkeyTrue, int monkeyFalse) {
            this.items = items;
            this.number = number;
            this.operator = operator;
            this.operation = operation;
            this.testCase = testCase;
            this.monkeyTrue = monkeyTrue;
            this.monkeyFalse = monkeyFalse;
            this.inspectedItems = 0;
        }

        public void addItem(long item) {
            items.addLast(item);
        }

        public boolean performTest(long item) {
            return item % testCase == 0;
        }


        public long getNextMonkey(long item) {
            if (performTest(item)) {
                return monkeyTrue;
            }
            return monkeyFalse;
        }

        public long addBoredom(long item) {
            return item / 3;
        }

        private long addMonkeyOperation(long item) {
            if (operator.equals("+")) {
                if (operation.equals("old")) {
                    item = item + item;
                } else {
                    item = item + Integer.parseInt(operation);
                }
            } else {
                if (operation.equals("old")) {
                    item = item * item;
                } else {
                    item = item * Integer.parseInt(operation);
                }
            }
            return item;
        }

        @Override
        public String toString() {
            return number + ": " + inspectedItems;
        }
    }

}
