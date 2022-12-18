package com.finallion;

import javax.swing.text.Position;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class DaySeventeen implements Day {
    private Set<Position> gameField = new HashSet<>();
    private boolean landed = false;

    @Override
    public void partOne() {
        readFile(true, 2023, 0);
        System.out.println(getHighestRock());
    }

    @Override
    public void partTwo() {
        // after x stones at height y start a recurring pattern with stone length a and height b.
        // solve ((1000000000000L - x) / a) * b + y;

        // pattern start somewhere around stone 976 and has a length of estimated 770 stones

        readFile(false, 1746, 976);
    }

    public void readFile(boolean partOne, long bounds, int cycleStart) {
        for (int i = 0; i < 7; i++) {
            gameField.add(new Position(i, 0));
        }

        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("17")));
            int type = 0;
            int movement;
            Rock rock = new Rock(type, 2, 4); // start at 4 because layer 1 is bottom
            long maxRocks = 1;
            int charCounter = 0;

            while (maxRocks < bounds) {
                movement = lines.get(0).charAt(charCounter);
                charCounter++;

                if (charCounter == lines.get(0).length()) {
                    charCounter = 0;
                }

                if (landed) {
                    if (maxRocks == cycleStart && !partOne) {
                        System.out.println(getHighestRock());
                    }
                    maxRocks++;
                    type++;
                    type = type % 5;
                    landed = false;
                    rock = new Rock(type, 2, getHighestRock() + 4);
                }

                rock.moveDirection((char)movement);
                rock.moveDown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getHighestRock() {
        int max = 0;
        for (Position pos : gameField) {
            if (pos.y > max) {
                max = pos.y;
            }
        }
        return max;
    }

    private void printGameField() {
        char[][] field = new char[2733][7];
        for (int i = 0; i < field.length; i++) {
            for (int ii = 0; ii < field[0].length; ii++) {
                field[i][ii] = '.';
            }
        }

        for (Position pos : gameField) {
            field[pos.y][pos.x] = '#';
        }

        for (int i = 0; i < field.length; i++) {
            for (int ii = 0; ii < field[0].length; ii++) {
                System.out.print(field[i][ii]);
            }
            System.out.println();
        }
        System.out.println("----------");
    }

    class Rock {
        int type;
        int x;
        int y;
        List<Position> collision;

        public Rock(int type, int startX, int startY) {
            this.type = type;
            this.x = startX;
            this.y = startY;
            this.collision = getCollisionBox(type, x, y);
        }

        public void moveDown() {
            if (!checkOutOfBounds(getCollisionBox(type, x, y - 1))) {
                if (checkCollision(getCollisionBox(type, x, y - 1))) {
                    gameField.addAll(collision);
                    landed = true;
                } else {
                    y--;
                    collision = getCollisionBox(type, x, y);
                }
            }
        }

        public void moveDirection(char direction) {
            if (direction == '<') {
                if (!checkOutOfBounds(getCollisionBox(type, x - 1, y))) {
                    if (!checkCollision(getCollisionBox(type, x - 1, y))) {
                        x--;
                        collision = getCollisionBox(type, x, y);
                    }
                }
            } else {
                if (!checkOutOfBounds(getCollisionBox(type, x + 1, y))) {
                    if (!checkCollision(getCollisionBox(type, x + 1, y))) {
                        x++;
                        collision = getCollisionBox(type, x, y);
                    }
                }
            }
        }

        public boolean checkCollision(List<Position> collision) {
            for (Position position : gameField) {
                for (Position piecePos : collision) {
                    if (position.x == piecePos.x && position.y == piecePos.y) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean checkOutOfBounds(List<Position> collision) {
            for (Position pos : collision) {
                if (pos.isOutOfBounds()) {
                    return true;
                }
            }
            return false;
        }

        public List<Position> getCollisionBox(int type, int x, int y) {
            List<Position> collisionBox = new ArrayList<>();
            switch (type) {
                default -> {
                    collisionBox.add(new Position(x + 0, y + 0));
                    collisionBox.add(new Position(x + 1, y + 0));
                    collisionBox.add(new Position(x + 2, y + 0));
                    collisionBox.add(new Position(x + 3, y + 0));
                }
                case 1 -> {
                    collisionBox.add(new Position(x + 1, y + 0));
                    collisionBox.add(new Position(x + 0, y + 1));
                    collisionBox.add(new Position(x + 1, y + 1));
                    collisionBox.add(new Position(x + 2, y + 1));
                    collisionBox.add(new Position(x + 1, y + 2));
                }
                case 2-> {
                    collisionBox.add(new Position(x + 0, y + 0));
                    collisionBox.add(new Position(x + 1, y + 0));
                    collisionBox.add(new Position(x + 2, y + 0));
                    collisionBox.add(new Position(x + 2, y + 1));
                    collisionBox.add(new Position(x + 2, y + 2));
                }
                case 3 -> {
                    collisionBox.add(new Position(x + 0, y + 0));
                    collisionBox.add(new Position(x + 0, y + 1));
                    collisionBox.add(new Position(x + 0, y + 2));
                    collisionBox.add(new Position(x + 0, y + 3));
                }
                case 4 -> {
                    collisionBox.add(new Position(x + 0, y + 0));
                    collisionBox.add(new Position(x + 1, y + 0));
                    collisionBox.add(new Position(x + 0, y + 1));
                    collisionBox.add(new Position(x + 1, y + 1));
                }
            }
            return collisionBox;
        }
    }

    class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean isOutOfBounds() {
            if (x < 0 || x > 6 || y < 0) {
                return true;
            }
            return false;
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

        @Override
        public String toString() {
            return "[" + x + "," + y + "]";
        }
    }

}
