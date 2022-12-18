package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DaySixteen implements Day {
    private Graph graph = new Graph();
    private Set<Valve> workingValves = new HashSet<>();

    @Override
    public void partOne() {
        readFile();

        for (Valve valve : graph.adjVertices.keySet()) {
            valve.findPaths(workingValves);
        }

        for (Valve valve : graph.adjVertices.keySet()) {
            System.out.println(valve.distances);
        }

        /*
         List<StateP1> stack = new LinkedList<>();
        stack.add(new StateP1(start, 30, 0, new ArrayList<>()));
        int best = 0;
        while (!stack.isEmpty()) {
            StateP1 state = stack.remove(0);
            boolean terminal = true;
            for (var way : state.cur.ways.entrySet()) {
                Integer dist = way.getValue();
                Valve target = way.getKey();
                if (state.minutes - dist > 0 && !state.opened.contains(target)) {
                    ArrayList<Valve> opened = new ArrayList<>(state.opened);
                    opened.add(target);
                    int newlyReleased = (state.minutes - dist) * target.flow;
                    terminal = false;
//                    System.out.println("at " + state + " using way " + target + "/" + dist + " freshly releasing " + newlyReleased);
                    stack.add(new StateP1(target, state.minutes - dist, state.released + newlyReleased, opened));
                }
            }
            if (terminal) {
                if (state.released > best) {
//                    System.out.println("new best: " + state);
                    best = state.released;
                }
            }
        }
        System.out.println("part 1: " + best);
         */

    }


    @Override
    public void partTwo() {

    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Sixteen")));

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                int flowRate = Integer.parseInt(line.split("=")[1].split(";")[0]);
                String name = line.substring(6, 8);

                Valve valve = new Valve(name, flowRate);
                graph.addVertex(valve);

                if (valve.flowRate > 0) {
                    workingValves.add(valve);
                }
            }


            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String name = line.substring(6, 8);
                String[] children = line.split(" ", 10)[9].trim().split(",");

                Valve src = graph.getVertex(name);
                for (String child : children) {
                    Valve dest = graph.getVertex(child.trim());
                    graph.addEdge(src, dest);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Graph {
        public Map<Valve, List<Valve>> adjVertices = new HashMap<>();

        public Valve getVertex(String name) {
            for (Valve key : adjVertices.keySet()) {
                if (key.name.equals(name)) {
                    return key;
                }
            }
            return null;
        }

        public void addVertex(Valve valve) {
            adjVertices.putIfAbsent(valve, new ArrayList<>());
        }

        public void addEdge(Valve src, Valve dest) {
            adjVertices.get(src).add(dest);
        }
    }

    private class Valve {
        int flowRate;
        String name;
        boolean open = false;
        public Map<Valve, Integer> distances = new HashMap<>();

        public Valve(String name, int flowRate) {
            this.flowRate = flowRate;
            this.name = name;
        }

        public void findPaths(Set<Valve> workingValves) {
            List<Valve> stack = new LinkedList<>();
            HashMap<Valve, Integer> distances = new HashMap<>();
            Set<Valve> visited = new HashSet<>();
            stack.add(this);
            distances.put(this, 0);
            while (!stack.isEmpty() && distances.size() < workingValves.size()) {
                Valve cur = stack.remove(0);
                visited.add(cur);
                Integer curDist = distances.get(cur);
                if (cur != this && workingValves.contains(cur)) {
                    distances.put(cur, curDist + 1); // the way takes curDist minutes; +1 minute for opening the valve
                }

                for (Valve v : graph.adjVertices.get(cur)) {
                    if (!visited.contains(v)) {
                        stack.add(v);
                        distances.put(v, curDist + 1);
                    }
                }
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            final Valve other = (Valve) obj;
            if (!this.name.equals(other.name)) {
                return false;
            }

            return true;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
