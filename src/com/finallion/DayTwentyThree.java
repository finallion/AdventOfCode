package com.finallion;

import com.finallion.util.Pos;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DayTwentyThree implements Day {
    List<Elf> elves = new ArrayList<>();
    Map<Pos, Integer> proposedPositionList = new HashMap<>();
    int lowerBound;
    int rightBound;

    private void calculateMovements(int rounds, boolean partOne) {

        for (int i = 0; i < rounds; i++) {
            for (Elf elf : elves) {
                findNeighboringElves(elf); // collect neighboring cells
                if (elf.neighboringElvesPos.size() != 8) { // size == 8 means all neighbor cells are free
                    proposeDirection(elf); // if not empty, propose move to next cell according to neighbor placement
                    elf.canMove = true;
                }
            }

            if (!partOne) {
                int size = elves.size();
                for (Elf elf : elves) {
                    if (!elf.canMove) {
                        size--;
                    }
                }

                if (size == 0) {
                    System.out.println("Round where no elf moves: " + i);
                    return;
                } else {
                    System.out.println("Round: " + i + " moving: " + size);
                }
            }

            // select move with the highest priority
            for (Elf elf : elves) {

                if (!elf.canMove) { // skip elves, that cannot move
                    continue;
                }

                for (Direction direction : elf.directions) {

                    if (elf.proposedPositions.containsKey(direction)) { // get the move with the highest priority
                        elf.updatedPos = elf.proposedPositions.get(direction);
                        elf.movedToDirection = direction;

                        if (!proposedPositionList.containsKey(elf.updatedPos)) { // count each proposed position from all elves
                            proposedPositionList.put(elf.updatedPos, 1);
                        } else {
                            proposedPositionList.computeIfPresent(elf.updatedPos, (k,v) -> v = v + 1);
                        }
                        break;
                    }
                }
            }


            // move, if no collision
            for (Elf elf : elves) {
                if (elf.updatedPos != null && elf.canMove) {
                    if (proposedPositionList.get(elf.updatedPos) == 1) { // check for collision
                        elf.currentPos = elf.updatedPos;
                    }
                }
                elf.updateDirectionArray();
                elf.proposedPositions.clear();
                elf.neighboringElvesPos.clear();
                elf.canMove = false;
                elf.updatedPos = null;
            }

            proposedPositionList.clear();
        }

        if (partOne) {
            int leftXBound = Integer.MAX_VALUE;
            int rightXBound = Integer.MIN_VALUE;
            int lowerYBound = Integer.MAX_VALUE;
            int upperYBound = Integer.MIN_VALUE;

            for (Elf elf : elves) {
                if (elf.currentPos.getX() < leftXBound) {
                    leftXBound = elf.currentPos.getX();
                }

                if (elf.currentPos.getX() > rightXBound) {
                    rightXBound = elf.currentPos.getX();
                }

                if (elf.currentPos.getY() < lowerYBound) {
                    lowerYBound = elf.currentPos.getY();
                }

                if (elf.currentPos.getY() > upperYBound) {
                    upperYBound = elf.currentPos.getY();
                }
            }


            int horizontalBound;
            int verticalBound;
            if (leftXBound < 0) {
                verticalBound = Math.abs(leftXBound) + Math.abs(rightXBound);
            } else {
                verticalBound = Math.abs(rightXBound) - Math.abs(leftXBound);
            }

            if (lowerYBound < 0) {
                horizontalBound = Math.abs(lowerYBound) + Math.abs(upperYBound);
            } else {
                horizontalBound = Math.abs(upperYBound) - Math.abs(lowerYBound);
            }

            int cells = (horizontalBound + 1) * (verticalBound + 1) - elves.size();
            System.out.println("Result: " + cells);
        }
    }


    @Override
    public void partOne() {
        //readFile();
        //calculateMovements(10, true);
    }

    @Override
    public void partTwo() {
        readFile();
        calculateMovements(1500, false);
    }


    private void proposeDirection(Elf elf) {
        Map<Direction, Pos> neighboringElvesPos = elf.neighboringElvesPos;

        if (neighboringElvesPos.containsKey(Direction.N) &&
                neighboringElvesPos.containsKey(Direction.NE) &&
                neighboringElvesPos.containsKey(Direction.NW)) {
            elf.proposedPositions.put(Direction.N, neighboringElvesPos.get(Direction.N));
        }

        if (neighboringElvesPos.containsKey(Direction.S) &&
                neighboringElvesPos.containsKey(Direction.SE) &&
                neighboringElvesPos.containsKey(Direction.SW)) {
            elf.proposedPositions.put(Direction.S, neighboringElvesPos.get(Direction.S));
        }

        if (neighboringElvesPos.containsKey(Direction.W) &&
                neighboringElvesPos.containsKey(Direction.NW) &&
                neighboringElvesPos.containsKey(Direction.SW)) {
            elf.proposedPositions.put(Direction.W, neighboringElvesPos.get(Direction.W));
        }

        if (neighboringElvesPos.containsKey(Direction.E) &&
                neighboringElvesPos.containsKey(Direction.NE) &&
                neighboringElvesPos.containsKey(Direction.SE)) {
            elf.proposedPositions.put(Direction.E, neighboringElvesPos.get(Direction.E));
        }
    }

    private void findNeighboringElves(Elf elf) {
        elf.neighboringElvesPos.put(Direction.NE, new Pos(elf.currentPos.getX() - 1, elf.currentPos.getY() + 1));
        elf.neighboringElvesPos.put(Direction.N, new Pos(elf.currentPos.getX() - 1, elf.currentPos.getY()));
        elf.neighboringElvesPos.put(Direction.NW, new Pos(elf.currentPos.getX() - 1, elf.currentPos.getY() - 1));
        elf.neighboringElvesPos.put(Direction.E, new Pos(elf.currentPos.getX(), elf.currentPos.getY() + 1));
        elf.neighboringElvesPos.put(Direction.W, new Pos(elf.currentPos.getX(), elf.currentPos.getY() - 1));
        elf.neighboringElvesPos.put(Direction.SE, new Pos(elf.currentPos.getX() + 1, elf.currentPos.getY() + 1));
        elf.neighboringElvesPos.put(Direction.S, new Pos(elf.currentPos.getX() + 1, elf.currentPos.getY()));
        elf.neighboringElvesPos.put(Direction.SW, new Pos(elf.currentPos.getX() + 1, elf.currentPos.getY() - 1));


        // remove positions with elves on
        for (Pos elfPos : elves.stream().map(e -> e.currentPos).collect(Collectors.toList())) {
            elf.neighboringElvesPos.keySet().removeIf(key -> elfPos.equals(elf.neighboringElvesPos.get(key)));
        }
    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("TwentyThree")));
            rightBound = lines.get(0).length();
            lowerBound = lines.size();

            for (int i = 0; i < lines.size(); i++) {
                for (int ii = 0; ii < lines.get(0).length(); ii++) {
                    if (lines.get(i).charAt(ii) == '#') {
                        elves.add(new Elf(new Pos<>(i, ii)));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    enum Direction {
        N, NE, NW, S, SE, SW, W, E
    }

    class Elf {
        Pos currentPos;
        Pos updatedPos = null;
        List<Direction> directions = new ArrayList<>();
        Direction movedToDirection;
        boolean canMove = false;
        Map<Direction, Pos> neighboringElvesPos = new HashMap<>();
        Map<Direction, Pos> proposedPositions = new HashMap<>();

        public Elf(Pos currentPos) {
            this.currentPos = currentPos;
            directions.add(Direction.N);
            directions.add(Direction.S);
            directions.add(Direction.W);
            directions.add(Direction.E);
        }

        public void updateDirectionArray() {
            Direction direction = directions.remove(0);
            directions.add(direction);
        }
    }
}
