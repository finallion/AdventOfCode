package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DayTwenty implements Day {

    @Override
    public void partOne() {
        List<Node> input = readFile(true);

        for (int i = 0; i < input.size(); i++) {
            Node current = new Node(-1, -1);
            int currentIndex = - 1;

            for (int ii = 0; ii < input.size(); ii++) {
                if (input.get(ii).index == i) {
                    current = input.get(ii);
                    currentIndex = ii;
                }
            }

            input.remove(currentIndex);
            input.add(Math.floorMod(current.value + currentIndex, input.size()), current);

            //printPretty(input);
        }

        int zeroIndex = input.indexOf(input.stream().filter(n -> n.value == 0).findFirst().get());
        long v1 = input.get((zeroIndex + 1000) % input.size()).value;
        long v2 = input.get((zeroIndex + 2000) % input.size()).value;
        long v3 = input.get((zeroIndex + 3000) % input.size()).value;

        System.out.println("Result: " + (v1 + v2 + v3));
    }

    private void printPretty(List<Node> nodes) {
        for (Node node : nodes) {
            System.out.print(node.value + " ");
        }
        System.out.println();
    }

    @Override
    public void partTwo() {
        List<Node> input = readFile(false);

        for (int i = 0; i < 10; i++) {
            for (int ii = 0; ii < input.size(); ii++) {
                Node current = new Node(-1, -1);
                int currentIndex = -1;

                for (int iii = 0; iii < input.size(); iii++) {
                    if (input.get(iii).index == ii) {
                        current = input.get(iii);
                        currentIndex = iii;
                    }
                }

                input.remove(currentIndex);
                input.add(Math.floorMod(current.value + currentIndex, input.size()), current);
            }
        }


        int zeroIndex = input.indexOf(input.stream().filter(n -> n.value == 0).findFirst().get());
        long v1 = input.get((zeroIndex + 1000) % input.size()).value;
        long v2 = input.get((zeroIndex + 2000) % input.size()).value;
        long v3 = input.get((zeroIndex + 3000) % input.size()).value;

        System.out.println("Result: " + (v1 + v2 + v3));
    }

    public List<Node> readFile(boolean partOne) {
        List<Node> input = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Twenty")));
            for (int i = 0; i < lines.size(); i++) {
                long value = Integer.parseInt(lines.get(i));
                if (!partOne) {
                    value *= 811589153;
                }
                input.add(new Node(i, value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    record Node(int index, long value) {
    }
}

