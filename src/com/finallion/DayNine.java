package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DayNine implements Day {
    private Position head = new Position(0, 0);
    private Position tail = new Position(0, 0);;
    private Position[] tails = new Position[9];
    private Set<Position> positions = new HashSet<>();

    @Override
    public void partOne() {
        readFile(1);
        System.out.println(positions.size());
    }

    @Override
    public void partTwo() {
        head = new Position(0,0);
        for (int i = 0; i < tails.length; i++) {
            tails[i] = new Position(0, 0);
        }
        positions.clear();

        readFile(2);
        System.out.println(positions.size());
    }

    private void moveHead(String direction) {
        switch (direction) {
            case "U" -> head.x--;
            case "D" -> head.x++;
            case "L" -> head.y--;
            case "R" -> head.y++;
        }
    }

    private void moveTails(Position leader, Position follower) {
        // diagonal
        if ((Math.abs(leader.x - follower.x) == 1 && Math.abs(leader.y - follower.y) > 1) || (Math.abs(leader.y - follower.y) == 1 && Math.abs(leader.x - follower.x) > 1) || (Math.abs(leader.x - follower.x) == 2 && Math.abs(leader.y - follower.y) == 2)) {
            if (leader.x > follower.x && leader.y > follower.y) {
                follower.x++;
                follower.y++;
            } else if (leader.x > follower.x && leader.y < follower.y) {
                follower.x++;
                follower.y--;
            } else if (leader.x < follower.x && leader.y > follower.y) {
                follower.x--;
                follower.y++;
            } else if (leader.x < follower.x && leader.y < follower.y) {
                follower.x--;
                follower.y--;
            }
        }
        // vertical
        else if (Math.abs(leader.x - follower.x) > 1 && Math.abs(leader.y - follower.y) == 0) {
            if (leader.x > follower.x) {
                follower.x++;
            } else {
                follower.x--;
            }
        }
        // horizontal
        else if (Math.abs(leader.y - follower.y) > 1 && Math.abs(leader.x - follower.x) == 0) {
            if (leader.y > follower.y) {
                follower.y++;
            } else {
                follower.y--;
            }
        }
    }

    public void readFile(int part) {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Nine")));
            for (String line : lines) {
                String[] parts = line.split(" ");

                for (int ii = 0; ii < Integer.parseInt(parts[1]); ii++) {
                    moveHead(parts[0]);
                    if (part == 1) {
                        moveTails(head, tail);
                        positions.add(new Position(tail.x, tail.y));
                    } else {
                        moveTails(head, tails[0]);
                        for (int iii = 1; iii < tails.length; iii++) {
                            moveTails(tails[iii - 1], tails[iii]);
                        }
                        positions.add(new Position(tails[8].x, tails[8].y));
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

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "[" + x + "," + y + "] ";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            final Position other = (Position) obj;
            if (x != other.x && y != other.y) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + this.x;
            hash = 53 * hash + this.y;
            return hash;
        }

    }
}

