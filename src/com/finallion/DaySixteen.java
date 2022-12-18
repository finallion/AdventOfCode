package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

// Thanks to https://github.com/dirk527/aoc2021/blob/main/src/aoc2022/Day16.java for the main ideas
public class DaySixteen implements Day {
    private Graph graph = new Graph();
    private Set<Valve> workingValves = new HashSet<>();
    private Valve start;

    @Override
    public void partOne() {
        readFile();

        for (Valve valve : graph.adjVertices.keySet()) {
            valve.findPaths(workingValves);
        }

        start = graph.getVertex("AA");
        start.findPaths(workingValves);

        List<StateP1> stack = new LinkedList<>();
        stack.add(new StateP1(start, 30, 0, new ArrayList<>())); // init state

        int best = 0;
        while (!stack.isEmpty()) { // BFS
            StateP1 state = stack.remove(0);
            boolean terminal = true;

            for (Map.Entry<Valve, Integer> way : state.current.distances.entrySet()) {
                Integer distance = way.getValue();
                Valve target = way.getKey();

                if (state.minutes - distance > 0 && !state.opened.contains(target)) {
                    ArrayList<Valve> opened = new ArrayList<>(state.opened);
                    opened.add(target);
                    int newlyReleased = (state.minutes - distance) * target.flowRate;
                    terminal = false;
                    stack.add(new StateP1(target, state.minutes - distance, state.released + newlyReleased, opened));
                }
            }
            if (terminal) {
                if (state.released > best) {
                    best = state.released;
                }
            }
        }
        System.out.println("Best route releases: " + best + " pressure.");
    }


    @Override
    public void partTwo() {
        Player startHuman = new Player(start, 26);
        Player startElephant = new Player(start, 26);
        List<StateP2> stack = new LinkedList<>();

        stack.add(new StateP2(startHuman, startElephant, 0, new ArrayList<>()));
        int best = 0;
        while (!stack.isEmpty()) {
            StateP2 state = stack.remove(0);

            // always move the player that is behind in time, or the human if tied
            boolean moveElephant = state.elephant.minutes > state.human.minutes;
            boolean terminal = addMoves(stack, state, moveElephant);

            // if the mover cannot move, maybe the other Player still can
            if (terminal) {
                terminal = addMoves(stack, state, !moveElephant);
            }

            if (terminal) {
                if (state.released > best) {
                    best = state.released;
                }
            }
            System.out.println("Best: " + best + " with size: " + stack.size());
        }
        System.out.println("Best route releases: " + best + " pressure.");
    }

    private static boolean addMoves(List<StateP2> stack, StateP2 state, boolean moveElephant) {
        boolean terminal = true;
        Player mover = moveElephant ? state.elephant : state.human;

        for (Map.Entry<Valve, Integer> way : mover.current.distances.entrySet()) {
            Integer dist = way.getValue();
            Valve target = way.getKey();

            if (mover.minutes - dist > 0 && !state.opened.contains(target)) {
                ArrayList<Valve> opened = new ArrayList<>(state.opened);
                opened.add(target);
                int newlyReleased = (mover.minutes - dist) * target.flowRate;
                terminal = false;
                Player newPlayer = new Player(target, mover.minutes - dist);
                stack.add(new StateP2(moveElephant ? state.human : newPlayer, moveElephant ? newPlayer : state.elephant, state.released + newlyReleased, opened));
            }
        }

        return terminal;
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
        public Map<Valve, Integer> distances = new HashMap<>();

        public Valve(String name, int flowRate) {
            this.flowRate = flowRate;
            this.name = name;
        }

        public void findPaths(Set<Valve> workingValves) {
            List<Valve> stack = new LinkedList<>();
            Set<Valve> visited = new HashSet<>();
            HashMap<Valve, Integer> distances = new HashMap<>();
            stack.add(this);
            distances.put(this, 0);

            while (!stack.isEmpty() && this.distances.size() < workingValves.size()) {
                Valve currentValve = stack.remove(0);
                visited.add(currentValve);
                Integer currentDistance = distances.get(currentValve);
                if (currentValve != this && workingValves.contains(currentValve)) {
                    this.distances.put(currentValve, currentDistance + 1); // the way takes currentDistance minutes; + 1 minute for opening the valve
                }

                for (Valve v : graph.adjVertices.get(currentValve)) {
                    if (!visited.contains(v)) {
                        stack.add(v);
                        distances.put(v, currentDistance + 1);
                    }
                }
            }

            System.out.println("For valve: " + this + " collect paths: " + distances);
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

    record StateP1(Valve current, int minutes, int released, Collection<Valve> opened) { }

    record StateP2(Player human, Player elephant, int released, Collection<Valve> opened) { }

    record Player(Valve current, int minutes) { }



}
