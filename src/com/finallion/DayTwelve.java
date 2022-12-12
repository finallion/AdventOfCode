package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DayTwelve implements Day {
    private Position[][] matrix;
    private Position start;
    private Position end;
    private List<Position> possibleStarters = new ArrayList<>();

    @Override
    public void partTwo() {
        List<Integer> pathDistances = new ArrayList<>();
        start.distance = Integer.MAX_VALUE; // set start to max, because it isn't the real starter anymore

        for (Position possibleStart : possibleStarters) { // iterate through all possible start positions
            Deque<Position> trail = new ArrayDeque<>();

            possibleStart.distance = 0; // set to 0, because it is the starter

            trail.push(possibleStart);
            Position current;
            // BFS-algorithm
            while ((current = trail.poll()) != null) {

                if (current.x == end.x && current.y == end.y) {
                    pathDistances.add(current.distance);
                    break;
                }

                visitNeighbor(current.x + 1, current.y, current, trail);
                visitNeighbor(current.x - 1, current.y, current, trail);
                visitNeighbor(current.x, current.y + 1, current, trail);
                visitNeighbor(current.x, current.y - 1, current, trail);
            }
        }

        pathDistances.sort(Integer::compareTo);
        System.out.println("Shortest distance: " + pathDistances.get(0));
    }

    @Override
    public void partOne() {
        readFile();

        Deque<Position> trail = new ArrayDeque<>();
        trail.push(start);
        Position current;
        int distToDest = 0;
        // BFS-algorithm
        while ((current = trail.poll()) != null) {
            if (current.x == end.x && current.y == end.y) {
                distToDest = current.distance;
                break;
            }

            // branch out in all directions
            visitNeighbor(current.x + 1, current.y, current, trail);
            visitNeighbor(current.x - 1, current.y, current, trail);
            visitNeighbor(current.x, current.y + 1, current, trail);
            visitNeighbor(current.x, current.y - 1, current, trail);
        }

        System.out.println("Shortest distance: " + distToDest);
    }

    private void visitNeighbor(int x, int y, Position current, Deque<Position> trail) {
        // can't find neighbor
        if (x < 0 || x >= matrix.length || y < 0 || y >= matrix[0].length || !isReachableHeight(current, matrix[x][y])) {
            return;
        }

        Position position = matrix[x][y];
        int distance = current.distance + 1;

        // go to next node, avoid circling
        if (distance < position.distance) {
            position.distance = distance;
            position.parent = current;
            trail.add(position);
	    }
    }

    private boolean isReachableHeight(Position current, Position pos) {
        return pos.value - current.value == 1 || pos.value - current.value <= 0;

    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Twelve")));
            matrix = new Position[lines.size()][lines.get(0).length()];

            for (int i = 0; i < lines.size(); i++) {
                String[] chars = lines.get(i).split("");
                for (int ii = 0; ii < chars.length; ii++) {
                    if (chars[ii].charAt(0) == 'S') {
                        start = new Position(i, ii, 'a', 0);
                        matrix[i][ii] = start;
                    } else if (chars[ii].charAt(0) == 'E') {
                        end = new Position(i, ii, 'z', Integer.MAX_VALUE);
                        matrix[i][ii] = end;
                    } else if (chars[ii].charAt(0) == 'a') { // part two stuff
                        Position starter = new Position(i, ii, 'a', Integer.MAX_VALUE);
                        possibleStarters.add(starter);
                        matrix[i][ii] = starter;
                    } else {
                        matrix[i][ii] = new Position(i, ii, chars[ii].charAt(0), Integer.MAX_VALUE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Position {
        int x;
        int y;
        char value;
        Position parent;
        int distance;

        public Position(int x, int y, char value, int distance) {
            this.x = x;
            this.y = y;
            this.value = value;
            this.parent = null;
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "[" + x + "/" + y + "]:" + value + "";
        }
    }
}
