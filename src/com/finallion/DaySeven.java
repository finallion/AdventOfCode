package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DaySeven implements Day {
    private Node root;
    private Stack<Node> tracker = new Stack<>();
    private Node match;
    private int finalVal = 0;
    private int level = 1; // needed to prevent the backtrackParent function to find higher dir with the same name as lower subsub dirs (Dir A -> Dir B -> Dir A)

    // part two
    private final int AVAILABLE_DISK_SPACE = 70000000;
    private final int NEEDED_SPACE = 30000000;
    private int necessaryFreedSpace;
    private List<Node> candidates = new ArrayList<>();


    @Override
    public void partOne() {
        System.out.println("PART ONE: ");
        readFile();
        System.out.println(root);
        print(root);
        System.out.println("Result: " + finalVal);
    }

    @Override
    public void partTwo() {
        System.out.println("PART TWO: ");
        readFile();
        necessaryFreedSpace = (NEEDED_SPACE - (AVAILABLE_DISK_SPACE - root.value));
        System.out.println("Required to delete: " + necessaryFreedSpace + ".");

        getCandidates(root);
        candidates.sort(Comparator.comparingInt(n -> n.value)) ;
        System.out.println("Delete dir " + candidates.get(0) + ".");
    }

    private void getCandidates(Node node) {
        for (Node child : node.children) {
            if (child.isDir) {
                if (child.value >= necessaryFreedSpace) {
                    candidates.add(child);
                }
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
                    level--;
                } else if (line.startsWith("$ cd")) {
                    backtrackParent(root, line.substring(5));
                    tracker.push(match);
                    level++;
                } else if (line.startsWith("$ ls")) {
                    continue;
                } else if (line.startsWith("dir")) {
                    String[] parts = line.split(" ");
                    // create dir node
                    Node node = new Node(parts[1], true, level);
                    Node parent = tracker.peek();
                    parent.addChild(node);
                    node.parent = parent;
                } else {
                    String[] parts = line.split(" ");
                    int value = Integer.parseInt(parts[0]);
                    // create file node
                    Node node = new Node(parts[1], value, false, level);
                    Node parent = tracker.peek();
                    parent.addChild(node);
                    node.parent = parent;

                    cascadeValue(node.parent, value); // adds the value to all parent nodes
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

    public void backtrackParent(Node child, String name) {
        if (child.name.equals(name) && level == child.level) {
            match = child;
        }

        for (Node each : child.children) {
            backtrackParent(each, name);
        }
    }

    public void print(Node node) {
        for (Node child : node.children) {
            if (child.isDir) {
                if (child.value <= 100000) {
                    System.out.println(child);
                    finalVal += child.value;
                }
            }
            print(child);
        }
    }




    public class Node {
        private String name;
        private int value;
        private Node parent;
        private List<Node> children = new ArrayList<>();
        private boolean isDir;
        private int level;

        public Node(String name, boolean isDir, int level) {
            this.name = name;
            this.value = 0;
            this.parent = null;
            this.isDir = isDir;
            this.level = level;
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

        @Override
        public String toString() {
            return name + "[" + value + "]";
        }
    }

}
