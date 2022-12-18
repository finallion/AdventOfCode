package com.finallion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Thanks to https://github.com/ash42/adventofcode/tree/main/adventofcode2022/src/nl/michielgraat/adventofcode2022/day16 for the main ideas
public class DaySixteen implements Day {

    private Map<State, Integer> cache = new HashMap<>();
    private List<Valve> valves = new ArrayList<>();
    private Valve start;

    private void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Sixteen")));
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                int flowRate = Integer.parseInt(line.split("=")[1].split(";")[0]);
                String name = line.substring(6, 8);
                String[] children = line.split(" ", 10)[9].trim().split(",");

                Valve valve = new Valve(name, flowRate);
                if (name.equals("AA")) {
                    start = valve;
                }

                for (String child : children) {
                    valve.addNeighbourName(child.trim());
                }
                valves.add(valve);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int calcPressure(Valve start, int minute, List<Valve> opened, List<Valve> valves, int nrOfOtherPlayers) {
        if (minute == 0) {
            Valve aa = valves.stream().filter(f -> f.getName().equals("AA")).findFirst().orElseThrow(NoSuchElementException::new);
            return nrOfOtherPlayers > 0 ? calcPressure(aa, 26, opened, valves, nrOfOtherPlayers - 1) : 0;
        }

        State state = new State(start, minute, opened, nrOfOtherPlayers);

        if (cache.keySet().contains(state)) {
            return cache.get(state);
        }

        int max = 0;
        if (start.getFlowRate() > 0 && !opened.contains(start)) {
            opened.add(start);
            Collections.sort(opened);
            int val = (minute - 1) * start.getFlowRate() + calcPressure(start, minute - 1, opened, valves, nrOfOtherPlayers);
            opened.remove(start);
            max = val;
        }

        for (String name : start.getNeighbourNames()) {
            Valve neighbour = valves.stream().filter(v -> v.getName().equals(name)).findFirst().orElseThrow(NoSuchElementException::new);
            max = Math.max(max, calcPressure(neighbour, minute - 1, opened, valves, nrOfOtherPlayers));
        }
        cache.put(state, max);

        return max;
    }

    public void partTwo() {
        cache = new HashMap<>();
        System.out.println(calcPressure(start, 26, new ArrayList<>(), valves, 1));
    }

    public void partOne() {
        readFile();
        cache = new HashMap<>();
        System.out.println(calcPressure(start, 30, new ArrayList<>(), valves, 0));
    }

    public record State(Valve valve, int minute, List<Valve> openValves, int nrOfOtherPlayers) {
    }


    public class Valve implements Comparable<Valve> {
        private final String name;
        private int flowRate;
        private final Set<String> neighbourNames = new HashSet<>();

        public Valve(final String name, final int flowRate) {
            this.name = name;
            this.flowRate = flowRate;
        }

        public void addNeighbourName(final String neighbourName) {
            neighbourNames.add(neighbourName);
        }

        public String getName() {
            return this.name;
        }

        public int getFlowRate() {
            return this.flowRate;
        }

        public Set<String> getNeighbourNames() {
            return this.neighbourNames;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Valve other = (Valve) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int compareTo(final Valve o) {
            return Integer.compare(this.flowRate, o.flowRate);
        }
    }
}
