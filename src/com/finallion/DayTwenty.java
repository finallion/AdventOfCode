package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DayTwenty implements Day {

    @Override
    public void partOne() {
        List<Node> input = readFile();
        for (int i = 0; i < input.size(); i++) {
            Node current = new Node(-1,-1);
            for (Node node : input) {
                if (node.index == i) {
                    current = node;
                }
            }
            if (current.index == -1) {
                break;
            }
            System.out.println("Current: " + current);

            int pos;
            if (current.value > 0) {
                pos = current.value + i;
                while (pos >= input.size()) {
                    pos -= input.size() - 1;
                }
            } else {
                pos = current.value - i;
                while (pos < 0) {
                    pos += input.size() - 1;
                }
            }
            System.out.println("Wants at pos: " + pos);


            Node last = input.get(input.size() - 1);
            System.out.println("Last node: " + last);
            System.out.println("Input before shift: " + input);
            // shift all from pos one pos right
            for (int ii = input.size() - 1; ii >= pos; ii--) {
                System.out.println("-------Shifting: " + input.get(ii) + " and " + input.get(ii - 1));
                input.set(ii, input.get(ii - 1));
            }

            System.out.println("Input after 1st shift: " + input);
            // add our node to its place
            input.set(pos, current);
            System.out.println("Input after adding: " + input);

            // shift all from 0 one pos right
            // add the previous last one back to pos 0
            for (int ii = pos - 1; ii > 0; ii--) {
                input.set(ii, input.get(ii - 1));
            }
            System.out.println("Input after shifting 2: " + input);
            input.set(0, last);
            System.out.println("Input after adding last: " + input);
        }

        System.out.println(input);
    }

    @Override
    public void partTwo() {
    }

    public List<Node> readFile() {
        List<Node> input = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Twenty")));
            for (int i = 0; i < lines.size(); i++) {
                input.add(new Node(i, Integer.parseInt(lines.get(i))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    class Node {
        int index;
        int value;

        public Node(int index, int value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public String toString() {
            return value + "";
        }
    }
}
