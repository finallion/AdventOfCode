package com.finallion;

import com.finallion.util.Position;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DayTwentyFour implements Day {
    private Position player;
    private Map<Position, WindType> winds = new HashMap<>();
    int maxCol;
    int maxRow;


    private boolean isWindFreeCell(Position position, int timePassed) {
        for (Position wind : winds.keySet()) {
            switch (winds.get(wind)) {
                case RIGHT -> {
                    int newX = wind.getX() + timePassed;
                    if (newX > maxCol) {
                        newX = newX % (maxCol + 1) + 1;
                    }

                    if (position.equals(new Position(newX, wind.getY()))) {
                        return false;
                    }
                }
                case LEFT -> {
                    int newX = wind.getX() - timePassed;
                    if (newX <= 0) {
                        newX = newX + maxCol;
                    }

                    if (position.equals(new Position(newX, wind.getY()))) {
                        return false;
                    }
                }
                case DOWN -> {
                    int newY = wind.getY() + timePassed;
                    if (newY > maxRow) {
                        newY = newY % (maxRow + 1) + 1;
                    }

                    if (position.equals(new Position(wind.getX(), newY))) {
                        return false;
                    }
                }
                case UP -> {
                    int newY = wind.getY() - timePassed;
                    if (newY <= 0) {
                        newY = newY + maxRow;
                    }

                    if (position.equals(new Position(wind.getX(), newY))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


    @Override
    public void partOne() {
        readFile();
        findPath();

        /*
        char[][] matrix = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int ii = 0; ii < 10; ii++) {
                matrix[i][ii] = '.';
                for (Wind wind : winds) {
                    if (wind.position.getX() == ii && wind.position.getY() == i) {
                        switch (wind.type) {
                            case DOWN -> matrix[i][ii] = 'v';
                            case UP -> matrix[i][ii] = '^';
                            case LEFT -> matrix[i][ii] = '<';
                            case RIGHT -> matrix[i][ii] = '>';
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int ii = 0; ii < 10; ii++) {
                System.out.print(matrix[i][ii]);
            }
            System.out.println();
        }

         */

    }


    @Override
    public void partTwo() {

    }

    private void findPath() {
        LinkedList<State> stack = new LinkedList<>();
        List<Position> visited = new ArrayList<>();

        visited.add(player);
        stack.add(new State(player, 0, visited));
        int minMoves = Integer.MAX_VALUE;

        while (!stack.isEmpty()) {
            State state = stack.remove(0);

            if (state.moves > minMoves) {
                continue;
            }

            if (!isWindFreeCell(state.position, state.moves)) {
                continue;
            }

            if (state.position.equals(new Position(maxCol, maxRow + 1))) {
                if (state.moves <= minMoves) {
                    System.out.println(state);
                    minMoves = state.moves;
                }
            }


            for (Position position : collectValidPositions(state)) {
                List<Position> visitedPos = new ArrayList<>(state.visited);
                visitedPos.add(position);

                stack.add(new State(position, state.moves + 1, visitedPos));
            }
        }
    }


    private boolean isValidPosition(Position position, int timePassed) {
        timePassed++;

        if (position.getX() == 1 && position.getY() == 0) {
            return true;
        }

        if (position.getX() == maxCol && position.getY() == maxRow + 1) {
            return true;
        }

        if (position.getX() >= 1 && position.getX() <= maxCol && position.getY() >= 1 && position.getY() <= maxRow && isWindFreeCell(position, timePassed)) {
            return true;
        }
        return false;
    }

    private List<Position> collectValidPositions(State state) {
        List<Position> nextPositions = new ArrayList<>();
        Position left = new Position(state.position.getX() - 1, state.position.getY());
        Position right = new Position(state.position.getX() + 1, state.position.getY());
        Position up = new Position(state.position.getX(), state.position.getY() - 1);
        Position down = new Position(state.position.getX(), state.position.getY() + 1);

        if (isValidPosition(up, state.moves)) {
            nextPositions.add(up);
        }
        if (isValidPosition(down, state.moves)) {
            nextPositions.add(down);
        }
        if (isValidPosition(left, state.moves)) {
            nextPositions.add(left);
        }
        if (isValidPosition(right, state.moves)) {
            nextPositions.add(right);
        }

        nextPositions.add(state.position); // wait
        return nextPositions;
    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("TwentyFour")));
            maxRow = lines.size() - 2;
            maxCol = lines.get(0).length() - 2;
            player = new Position(1, 0);

            for (int i = 0; i < lines.size(); i++) {
                char[] signs = lines.get(i).toCharArray();
                for (int ii = 0; ii < signs.length; ii++) {
                    if (signs[ii] == '^') {
                        winds.put(new Position(ii, i), WindType.UP);
                    } else if (signs[ii] == 'v') {
                        winds.put(new Position(ii, i), WindType.DOWN);
                    } else if (signs[ii] == '<') {
                        winds.put(new Position(ii, i), WindType.LEFT);
                    } else if (signs[ii] == '>') {
                        winds.put(new Position(ii, i), WindType.RIGHT);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    record State(Position position, int moves, List<Position> visited) {
    }



    enum WindType {
        LEFT, RIGHT, UP, DOWN
    }
}
