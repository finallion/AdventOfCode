package com.finallion;

import com.finallion.util.Position;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DayTwentyFour implements Day {
    private static Set<Position> walls;
    private static Map<Position, List<WindType>> winds;
    private static int maxRows;
    private static int maxCols;

    public void readFile() {
        winds = new HashMap<>();
        walls = new HashSet<>();

        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("TwentyFour")));

            maxRows = lines.size();
            maxCols = lines.get(0).length();

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                for (int ii = 0; ii < line.length(); ii++) {
                    char cell = line.charAt(ii);
                    Position pos = Position.of(i, ii);

                    if (cell == '#') {
                        walls.add(pos);
                    } else if (cell != '.') {
                        switch (cell) {
                            case '^' -> winds.put(pos, List.of(WindType.UP));
                            case 'v' -> winds.put(pos, List.of(WindType.DOWN));
                            case '>' -> winds.put(pos, List.of(WindType.RIGHT));
                            case '<' -> winds.put(pos, List.of(WindType.LEFT));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int move(Position start, Position target) {
        Set<Position> currentPossible = new HashSet<>();
        currentPossible.add(start);
        int currentMoves = 0;

        while (!currentPossible.isEmpty()) {

            if (currentPossible.contains(target)) {
                break;
            }

            Map<Position, List<WindType>> movedCyclones = moveWinds();
            Set<Position> newPossible = new HashSet<>();

            for (Position position : currentPossible) {

                Set<Position> neighbors = getNeighbours(position, movedCyclones);
                newPossible.addAll(neighbors);

                if (!movedCyclones.containsKey(position)) {
                    newPossible.add(position);
                }
            }

            currentPossible = newPossible;
            winds = movedCyclones;
            currentMoves++;
        }

        return currentMoves;
    }

    private static Set<Position> getNeighbours(Position position, Map<Position, List<WindType>> winds) {
        return Set.of(
                        position.moveRight(),
                        position.moveLeft(),
                        position.moveUp(),
                        position.moveDown()).stream()
                .filter(pos -> pos.isValid(maxRows, maxCols) && !winds.containsKey(pos) && !walls.contains(pos))
                .collect(Collectors.toSet());
    }

    private static Map<Position, List<WindType>> moveWinds() {
        Map<Position, List<WindType>> moved = new HashMap<>();

        for (Map.Entry<Position, List<WindType>> entry : winds.entrySet()) {
            Position pos = entry.getKey();
            List<WindType> directions = entry.getValue();

            for (WindType direction : directions) {
                Position newPos = moveWind(pos, direction);

                if (walls.contains(newPos)) {
                    newPos = wrapAround(pos, direction);
                }

                moved.putIfAbsent(newPos, new ArrayList<>());
                moved.get(newPos).add(direction);
            }
        }
        return moved;
    }

    private static Position moveWind(Position windPosition, WindType direction) {
        switch (direction) {
            case RIGHT: return windPosition.moveRight();
            case LEFT: return windPosition.moveLeft();
            case DOWN: return windPosition.moveDown();
            default: return windPosition.moveUp();
        }
    }

    private static Position wrapAround(Position windPosition, WindType direction) {
        switch (direction) {
            case RIGHT: return Position.of(windPosition.row(), 1);
            case LEFT: return Position.of(windPosition.row(), maxCols - 2);
            case DOWN: return Position.of(1, windPosition.col());
            default: return Position.of(maxRows - 2, windPosition.col());
        }
    }

    enum WindType {
        LEFT, RIGHT, UP, DOWN
    }

    @Override
    public void partOne() {
        readFile();

        Position start = Position.of(0, 1);
        Position target = Position.of(maxRows - 1, maxCols - 2);

        int sum = move(start, target);
        System.out.println(sum);
    }

    @Override
    public void partTwo() {
        readFile();

        Position start = Position.of(0, 1);
        Position target = Position.of(maxRows - 1, maxCols - 2);

        int sum = move(start, target);
        sum += move(target, start);
        sum += move(start, target);
        System.out.println(sum);
    }


}
