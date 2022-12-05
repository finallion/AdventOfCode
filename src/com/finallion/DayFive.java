package com.finallion;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayFive implements Day {

    @Override
    public void partOne() {
        //System.out.println("The result is: " + readFile(9));
    }

    @Override
    public void partTwo() {
        System.out.println("The result is: " + readFile(9));
    }

    public String readFile(int howManyStacks) {
        //List<String> lines = Files.readAllLines(Path.of("input.txt"));

        File file = new File(this.buildPath("Five"));
        List<Deque<String>> stacks = new ArrayList<>();
        Pattern cratePattern = Pattern.compile("\\[[a-zA-Z]]|\\s{4}");
        Pattern instructionPattern = Pattern.compile("(\\d+)");

        for (int i = 0; i < howManyStacks; i++) {
            stacks.add(i, new ArrayDeque<>());
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // read stacks of crates
                if (!line.startsWith("move")) {
                    int counter = 0;
                    Matcher matcher = cratePattern.matcher(line);
                    while (matcher.find()) {
                        if (matcher.group().isBlank()) {
                            counter++;
                            continue;
                        }
                        stacks.get(counter).push(matcher.group());
                        counter++;
                    }
                } else { // read instructions
                    Matcher matcher = instructionPattern.matcher(line);
                    int howMany = 0, from = 0, to = 0;

                    if (matcher.find()) { howMany = Integer.parseInt(matcher.group()); }
                    if (matcher.find()) { from = Integer.parseInt(matcher.group()); }
                    if (matcher.find()) { to = Integer.parseInt(matcher.group()); }

                    // part two
                    Deque<String> inOrder = new ArrayDeque<>();
                    for (int i = 0; i < howMany; i++) {
                        String crate = stacks.get(from - 1).pollLast();
                        inOrder.addFirst(crate);
                    }

                    for (String crate : inOrder) {
                        stacks.get(to - 1).addLast(crate);
                    }

                    // part one
                    /*
                    for (int i = 0; i < howMany; i++) {
                        String crate = stacks.get(from - 1).pollLast();
                        stacks.get(to - 1).addLast(crate);
                    }
                     */
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = "";
        for (Deque<String> deque: stacks) {
            if (deque.isEmpty()) {
                continue;
            }
            result += deque.pollLast().substring(1, 2);
        }
        return result;
    }
}
