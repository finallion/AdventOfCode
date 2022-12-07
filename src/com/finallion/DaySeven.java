package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DaySeven implements Day {
    private Node root;
    private Stack<Node> tracker = new Stack<>();
    private Node match;
    private int finalVal = 0;
    private List<Node> candidates = new ArrayList<>();

    @Override
    public void partOne() {
        readFile();
        collectMatchingDirs(root);
        System.out.println("Result: " + finalVal + ".");
    }

    @Override
    public void partTwo() {
        readFile();
        getCandidates(root);
        candidates.sort(Comparator.comparingInt(n -> n.value));
        System.out.println("Delete dir " + candidates.get(0).name + " with value: " + candidates.get(0).value + ".");
    }

    private void getCandidates(Node node) {
        for (Node child : node.children) {
            if (child.isDir && child.value >= (30000000 - (70000000 - root.value))) {
                candidates.add(child);
            }
            getCandidates(child);
        }
    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Seven")));
            root = new Node("/", 0, true, 0);
            tracker.push(root); // tracks the current parent

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.startsWith("$ cd ..")) {
                    tracker.pop();
                } else if (line.startsWith("$ cd")) {
                    backtrackParent(root, line.substring(5)); // there is surely a better way, I'm just not able to find it
                    tracker.push(match);
                } else if (line.startsWith("$ ls")) {
                    continue;
                } else {
                    Node parent = tracker.peek();
                    String[] parts = line.split(" ");
                    Node node;

                    if (line.startsWith("dir")) { // create dir
                        node = new Node(parts[1], true, tracker.size());
                    } else { // create file
                        int value = Integer.parseInt(parts[0]);
                        node = new Node(parts[1], value, false, tracker.size());
                        cascadeValue(parent, value); // adds the value to all parent nodes
                    }
                    parent.addChild(node);
                    node.parent = parent;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cascadeValue(Node parent, int value) {
        while (parent != null) {
            parent.value += value;
            parent = parent.parent;
        }
    }

    private void backtrackParent(Node child, String name) {
        if (child.name.equals(name) && tracker.size() == child.level) {
            match = child;
        }

        for (Node each : child.children) {
            backtrackParent(each, name);
        }
    }

    private void collectMatchingDirs(Node node) {
        for (Node child : node.children) {
            if (child.isDir && child.value <= 100000) {
                finalVal += child.value;
            }
            collectMatchingDirs(child);
        }
    }

    private class Node {
        private String name;
        private int value;
        private Node parent;
        private List<Node> children = new ArrayList<>();
        private boolean isDir;
        private int level;

        public Node(String name, boolean isDir, int level) {
            this(name, 0, isDir, level);
        }

        public Node(String name, int value, boolean isDir, int level) {
            this.name = name;
            this.value = value;
            this.parent = null;
            this.isDir = isDir;
            this.level = level;
        }

        public void addChild(Node child) {
            children.add(child);
        }
    }
}
