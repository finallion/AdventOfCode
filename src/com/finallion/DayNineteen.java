package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayNineteen implements Day {
    private List<Blueprint> blueprints = new ArrayList<>();
    private Blueprint currentPrint;
    private int currentMax = 0;

    @Override
    public void partOne() {
        readFile();
        int result = 0;
        for (int i = 0; i < blueprints.size(); i++) {
            currentPrint = blueprints.get(i);
            int geodes = runFactory(23);
            System.out.println("Found a max of " + geodes + " geodes in blueprint " + (i + 1));
            result += geodes * (i + 1);
            if (geodes > currentMax) {
                currentMax = geodes;
            }
        }

        System.out.println("Result: " + result);
    }

    @Override
    public void partTwo() {
        readFile();
        int result = 1;
        for (int i = 0; i < 3; i++) {
            currentPrint = blueprints.get(i);
            int geodes = runFactory(31);
            System.out.println("Found a max of " + geodes + " geodes in blueprint " + (i + 1));
            result *= geodes;
            if (geodes > currentMax) {
                currentMax = geodes;
            }
        }

        System.out.println("Result: " + result);
    }

    private int runFactory(int startTime) {
        LinkedList<State> stack = new LinkedList<>();
        stack.add(new State(startTime, 1, 0, 0, 0, 1, 0, 0, 0, false, null));
        int maxGeodes = 0;

        while (!stack.isEmpty()) { // DFS
            State state = stack.remove(0);

            // break search
            if (state.time == 0) {
                break;
            }

            // collect resources
            int ore = state.oreProduced + state.oreRobots;
            int clay = state.clayProduced + state.clayRobots;
            int obsidian = state.obsidianProduced + state.obsidianRobots;
            int geodes = state.geodesProduced + state.geodeRobots;


            // can the path achieve the current max even if it produced a geode robot every round?
            if (optimisticBest(geodes, state.geodeRobots, state.time) <= maxGeodes) {
                continue;
            }

            // can the path produce enough obsidian to build geode robots to achieve the max if it built an obsidian robot every round?
            int minObsidianNeeded = (maxGeodes - optimisticBest(geodes, state.geodeRobots, state.time)) * currentPrint.costGeodeRobotObsidian;
            if (optimisticBest(obsidian, state.obsidianRobots, state.time) < minObsidianNeeded) {
                continue;
            }

            // new max found
            if (geodes > maxGeodes) {
                maxGeodes = geodes;
            }

            // get all buildable robot types
            List<RobotType> buildableRobots = getBuildableRobots(state);
            for (RobotType robotType : buildableRobots) {
                // the state waited to build a robot, although it had enough resources and tried to build it in the next round
                if (state.waitedAlthoughCouldBuy && state.types != null) {
                    if (state.types.contains(robotType) && robotType != RobotType.NO_ROBOT) {
                        break;
                    }
                }

                int oreCopy = ore;
                int clayCopy = clay;
                int obsidianCopy = obsidian;

                // built robot
                switch (robotType) {
                    case ORE -> {
                        oreCopy -= currentPrint.costOreRobot;
                        stack.add(new State(state.time - 1, oreCopy, clayCopy, obsidianCopy, geodes, state.oreRobots + 1, state.clayRobots, state.obsidianRobots, state.geodeRobots, false, null));
                    }
                    case CLAY -> {
                        oreCopy -= currentPrint.costClayRobot;
                        stack.add(new State(state.time - 1, oreCopy, clayCopy, obsidianCopy, geodes, state.oreRobots, state.clayRobots + 1, state.obsidianRobots, state.geodeRobots, false, null));
                    }
                    case OBSIDIAN -> {
                        oreCopy -= currentPrint.costObsidianRobotOre;
                        clayCopy -= currentPrint.costObsidianRobotClay;
                        stack.add(new State(state.time - 1, oreCopy, clayCopy, obsidianCopy, geodes, state.oreRobots, state.clayRobots, state.obsidianRobots + 1, state.geodeRobots, false, null));
                    }
                    case GEODE -> {
                        oreCopy -= currentPrint.costGeodeRobotOre;
                        obsidianCopy -= currentPrint.costGeodeRobotObsidian;
                        stack.add(new State(state.time - 1, oreCopy, clayCopy, obsidianCopy, geodes, state.oreRobots, state.clayRobots, state.obsidianRobots, state.geodeRobots + 1, false, null));
                    }
                    default -> {
                        // it can build another robot, but the state will wait
                        if (buildableRobots.size() > 1) { // we pass the buildable robots so we can check next round if it wants to build the same robot again
                            stack.add(new State(state.time - 1, oreCopy, clayCopy, obsidianCopy, geodes, state.oreRobots, state.clayRobots, state.obsidianRobots, state.geodeRobots, true, buildableRobots));
                        } else {
                            stack.add(new State(state.time - 1, oreCopy, clayCopy, obsidianCopy, geodes, state.oreRobots, state.clayRobots, state.obsidianRobots, state.geodeRobots, false, null));
                        }
                    }
                }

            }
        }
        return maxGeodes;
    }

    private int optimisticBest(int material, int robotsForMaterial, int time) {
        return material + robotsForMaterial * time + time * (time - 1) / 2; // triangular number
    }

    public void readFile() {
        Pattern pattern = Pattern.compile("\\d+");
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Nineteen")));

            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                Blueprint blueprint = new Blueprint();

                matcher.find();
                if (matcher.find()) blueprint.costOreRobot = Integer.parseInt(matcher.group());
                if (matcher.find()) blueprint.costClayRobot = Integer.parseInt(matcher.group());
                if (matcher.find()) blueprint.costObsidianRobotOre = Integer.parseInt(matcher.group());
                if (matcher.find()) blueprint.costObsidianRobotClay = Integer.parseInt(matcher.group());
                if (matcher.find()) blueprint.costGeodeRobotOre = Integer.parseInt(matcher.group());
                if (matcher.find()) blueprint.costGeodeRobotObsidian = Integer.parseInt(matcher.group());

                blueprints.add(blueprint);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<RobotType> getBuildableRobots(State state) {
        List<RobotType> robotTypes = new ArrayList<>();

        // if geode robot can be build, build it!
        if (state.oreProduced >= currentPrint.costGeodeRobotOre && state.obsidianProduced >= currentPrint.costGeodeRobotObsidian) {
            robotTypes.add(RobotType.GEODE);
            return robotTypes;
        }

        robotTypes.add(RobotType.NO_ROBOT);

        // last round is unnecessary building anything
        if (state.time == 1) {
            return robotTypes;
        }

        if (state.oreProduced >= currentPrint.costObsidianRobotOre && state.clayProduced >= currentPrint.costObsidianRobotClay && state.time > 2) {
            robotTypes.add(RobotType.OBSIDIAN);
        }

        // don't build more bots if you can't spend more of the resources they produce in a minute
        int oreProductionNecessary = currentPrint.costClayRobot + currentPrint.costObsidianRobotOre + currentPrint.costGeodeRobotOre;
        // and don't build more robots if the resource is abundant enough to build every robot,
        // this last statement can cause in very specific cases that the answer is off by one geode if implemented in obsidian and clay. Here in ore, it works...
        // this one makes the code run really fast.
        if (state.oreProduced >= currentPrint.costOreRobot && state.time > 2 && state.oreRobots <= oreProductionNecessary && state.oreProduced <= oreProductionNecessary) {
            robotTypes.add(RobotType.ORE);
        }

        if (state.oreProduced >= currentPrint.costClayRobot && state.time > 2 && state.clayRobots <= currentPrint.costObsidianRobotClay) {
            robotTypes.add(RobotType.CLAY);
        }

        return robotTypes;
    }

    record State(int time, int oreProduced, int clayProduced, int obsidianProduced, int geodesProduced, int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots, boolean waitedAlthoughCouldBuy, List<RobotType> types) {
    }

    static class Blueprint {
        int costOreRobot = 0;
        int costClayRobot = 0;
        int costObsidianRobotOre = 0;
        int costObsidianRobotClay = 0;
        int costGeodeRobotOre = 0;
        int costGeodeRobotObsidian = 0;
    }

    enum RobotType {
        GEODE, ORE, CLAY, OBSIDIAN, NO_ROBOT
    }
}
