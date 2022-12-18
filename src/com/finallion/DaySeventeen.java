package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DaySeventeen implements Day {
    private Set<Position> gameField = new HashSet<>();
    private boolean landed = false;
    private List<String> gameFieldInList = new ArrayList<>();
    private int rockIndexOfPatternStart = 0;
    private int rockIndexOfPatternEnd = 0;
    private int patternStartIndex = 0;
    private int patternEndIndex = 0;
    private int patternLength = 0;

    @Override
    public void partOne() {
        //readFile(true, 2023, 0);
        //printGameField();
        //System.out.println(getHighestRock());
    }

    @Override
    public void partTwo() {
        getStartOfPatternIndex();
        gameField.clear();
        readFile(false, 5000);
        System.out.println("Pattern start at height: " + patternStartIndex + " at stone: " + rockIndexOfPatternStart);
        System.out.println("Pattern ends at height: " + patternEndIndex + " at stone: " + rockIndexOfPatternEnd);
        System.out.println("Pattern has length of: " + patternLength + " with " + (rockIndexOfPatternEnd - rockIndexOfPatternStart) + " stones");

        // solution: 1581449275319
        // mine: 1581449275328, off by 9
        System.out.println(((1000000000000L - patternLength) / (rockIndexOfPatternEnd - (rockIndexOfPatternStart))) * patternLength + patternLength + patternStartIndex + rockIndexOfPatternStart);

    }

    private void getStartOfPatternIndex() {
        readFile(true, 5000);
        printGameField();

        for (int i = 0; i < gameFieldInList.size() - 21; i++) {
            for (int ii = 0; ii < gameFieldInList.size() - 21; ii++) {
                if (i == ii) continue;

                if (gameFieldInList.get(i).equals(gameFieldInList.get(ii)) &&
                        gameFieldInList.get(i + 1).equals(gameFieldInList.get(ii + 1)) &&
                        gameFieldInList.get(i + 2).equals(gameFieldInList.get(ii + 2)) &&
                        gameFieldInList.get(i + 3).equals(gameFieldInList.get(ii + 3)) &&
                        gameFieldInList.get(i + 4).equals(gameFieldInList.get(ii + 4)) &&
                        gameFieldInList.get(i + 5).equals(gameFieldInList.get(ii + 5)) &&
                        gameFieldInList.get(i + 6).equals(gameFieldInList.get(ii + 6)) &&
                        gameFieldInList.get(i + 7).equals(gameFieldInList.get(ii + 7)) &&
                        gameFieldInList.get(i + 8).equals(gameFieldInList.get(ii + 8)) &&
                        gameFieldInList.get(i + 9).equals(gameFieldInList.get(ii + 9)) &&
                        gameFieldInList.get(i + 10).equals(gameFieldInList.get(ii + 10)) &&
                        gameFieldInList.get(i + 11).equals(gameFieldInList.get(ii + 11)) &&
                        gameFieldInList.get(i + 12).equals(gameFieldInList.get(ii + 12)) &&
                        gameFieldInList.get(i + 13).equals(gameFieldInList.get(ii + 13)) &&
                        gameFieldInList.get(i + 14).equals(gameFieldInList.get(ii + 14)) &&
                        gameFieldInList.get(i + 15).equals(gameFieldInList.get(ii + 15)) &&
                        gameFieldInList.get(i + 16).equals(gameFieldInList.get(ii + 16)) &&
                        gameFieldInList.get(i + 17).equals(gameFieldInList.get(ii + 17)) &&
                        gameFieldInList.get(i + 18).equals(gameFieldInList.get(ii + 18)) &&
                        gameFieldInList.get(i + 19).equals(gameFieldInList.get(ii + 19)) &&
                        gameFieldInList.get(i + 20).equals(gameFieldInList.get(ii + 20))
                        ) {
                    patternStartIndex = i + 2; // add two because first line is ground and pattern starts after this match
                    patternLength = ii - i;
                    patternEndIndex = ii;
                    return;
                }
            }
        }
    }

    public void readFile(boolean partOne, long bounds) {
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


                if (!partOne && (getHighestRock() == patternStartIndex)) {
                    rockIndexOfPatternStart = (int) maxRocks + 1; // add current rock
                }

                if (!partOne && (getHighestRock() == patternEndIndex)) {
                    rockIndexOfPatternEnd = (int) maxRocks + 3; // add two because of previous rockIndexOfPatternStart and one for the actual pattern end
                    return;
                }

                if (landed) {
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
        char[][] field = new char[10000][7];
        for (int i = 0; i < field.length; i++) {
            for (int ii = 0; ii < field[0].length; ii++) {
                field[i][ii] = '.';
            }
        }

        for (Position pos : gameField) {
            field[pos.y][pos.x] = '#';
        }

        for (int i = 0; i < field.length; i++) {
            String line = "";
            //System.out.print(i + " ");
            for (int ii = 0; ii < field[0].length; ii++) {
                line += field[i][ii];
                System.out.print(field[i][ii]);
            }
            gameFieldInList.add(line);

            System.out.println();
        }
        //System.out.println("----------");
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
