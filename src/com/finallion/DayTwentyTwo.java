package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayTwentyTwo implements Day {
    char[][] matrix;
    String instructions;
    int currentRow = 0;
    int currentCol = 0;
    Direction facing = Direction.RIGHT;

    @Override
    public void partOne() {
        readFile();

        Pattern instruction = Pattern.compile("\\d+|L|R");
        Matcher matcher = instruction.matcher(instructions);
        Direction direction = Direction.RIGHT;

        while (matcher.find()) {
            String toDo = matcher.group();
            if (toDo.equals("L")) {
                direction = getNextDirection(direction, true);
            } else if (toDo.equals("R")) {
                direction = getNextDirection(direction, false);
            } else {
                int amount = Integer.parseInt(toDo);
                movePartOne(amount, direction);
            }
        }

        System.out.println("Result: " + (((currentRow + 1) * 1000) + ((currentCol + 1) * 4) + direction.ordinal()));
    }

    @Override
    public void partTwo() {
        readFile();

        Pattern instruction = Pattern.compile("\\d+|L|R");
        Matcher matcher = instruction.matcher(instructions);

        while (matcher.find()) {
            String toDo = matcher.group();
            if (toDo.equals("L")) {
                facing = getNextDirection(facing, true);
            } else if (toDo.equals("R")) {
                facing = getNextDirection(facing, false);
            } else {
                movePartTwo(Integer.parseInt(toDo));
            }
        }

        System.out.println("Result: " + (((currentRow + 1) * 1000) + ((currentCol + 1) * 4) + facing.ordinal()));
    }

    private void updateDirectionAndPosition() {
        int updatedRow = -1;
        int updatedCol = -1;
        Direction updatedFacing = facing;

        if (currentRow < 50) {
            if (currentCol < 100) {
                System.out.println("INSIDE 1");
                switch (facing) { // 1: UP FACE
                    case LEFT -> { // 4
                        updatedRow = 100 + (49 - currentRow);
                        updatedCol = 0; // max left
                        updatedFacing = Direction.RIGHT;
                    }
                    case UP -> { // 6
                        updatedRow = 199 - (99 - currentCol);
                        updatedCol = 0; // max left
                        updatedFacing = Direction.RIGHT;
                    }
                }

            } else {
                System.out.println("INSIDE 2");
                switch (facing) { // 2: EAST FACE
                    case RIGHT -> { // 5
                        updatedRow = 149 - currentRow;
                        updatedCol = 99;
                        updatedFacing = Direction.LEFT;
                    }
                    case DOWN -> { // 3
                        updatedRow = (currentCol - 99) + 49;
                        updatedCol = 99;
                        updatedFacing = Direction.LEFT;
                    }
                    case UP -> { // 6
                        updatedCol = 49 - (149 - currentCol);
                        updatedRow = 199;
                        updatedFacing = Direction.UP;
                    }
                }
            }
        } else if (currentRow < 100) { // 3: NORTH FACE
            System.out.println("INSIDE 3");
            switch (facing) {
                case RIGHT -> { // 2
                    updatedRow = 49;
                    updatedCol = 149 - (99 - currentRow);
                    updatedFacing = Direction.UP;
                }
                case LEFT -> { // 4
                    updatedRow = 100;
                    updatedCol = 49 - (99 - currentRow);
                    updatedFacing = Direction.DOWN;
                }
            }
        } else if (currentRow < 150) {
            if (currentCol < 50) {  // 4: WEST FACE
                System.out.println("INSIDE 4");
                switch (facing) {
                    case LEFT -> { // 1
                        updatedRow = (149 - currentRow);
                        updatedCol = 50;
                        updatedFacing = Direction.RIGHT;
                    }
                    case UP -> { // 3
                        updatedCol = 50;
                        updatedRow = 99 - (49 - currentCol);
                        updatedFacing = Direction.RIGHT;
                    }
                }
            } else { // 5: DOWN FACE
                System.out.println("INSIDE 5");
                switch (facing) {
                    case RIGHT -> { // 2
                        updatedCol = 149;
                        updatedRow = 149 - currentRow;
                        updatedFacing = Direction.LEFT;
                    }
                    case DOWN -> { // 6
                        updatedCol = 49;
                        updatedRow = 199 - (99 - currentCol);
                        updatedFacing = Direction.LEFT;
                    }
                }
            }
        } else { // 6: SOUTH FACE
            System.out.println("INSIDE 6");
            switch (facing) {
                case RIGHT -> { // 5
                    updatedRow = 149;
                    updatedCol = 99 - (199 - currentRow);
                    updatedFacing = Direction.UP;
                }
                case DOWN -> { // 2
                    updatedRow = 0;
                    updatedCol = 100 + currentCol;
                    updatedFacing = Direction.DOWN;
                }
                case LEFT -> { // 1
                    updatedRow = 0;
                    updatedCol = 99 - (199 - currentRow);
                    updatedFacing = Direction.DOWN;
                }
            }
        }

        if (updatedCol == -1 || updatedRow == -1) {
            return;
        }

        if (matrix[updatedRow][updatedCol] == '#') {
            return;
        } else {
            currentRow = updatedRow;
            currentCol = updatedCol;
            facing = updatedFacing;
        }
    }

    private void movePartTwo(int amount) {
        for (int i = 0; i < amount; i++) {
            switch (facing) {
                case RIGHT -> {
                    if (currentCol + 1 < matrix[0].length) { // target is in bounds
                        matrix[currentRow][currentCol] = '>';
                        if (isAllowedTile(matrix[currentRow][currentCol + 1])) { // target is on the actual field
                            currentCol++;
                        } else if (matrix[currentRow][currentCol + 1] == '#') { // target is wall
                        } else updateDirectionAndPosition();// target is in bounds but outside the field
                    } else updateDirectionAndPosition();
                }

                case LEFT -> {
                    if (currentCol - 1 >= 0) {
                        matrix[currentRow][currentCol] = '<';
                        if (isAllowedTile(matrix[currentRow][currentCol - 1])) {
                            currentCol--;
                        } else if (matrix[currentRow][currentCol - 1] == '#') {
                        } else updateDirectionAndPosition();
                    } else updateDirectionAndPosition();
                }
                case DOWN -> {
                    if (currentRow + 1 < matrix.length) {
                        matrix[currentRow][currentCol] = 'v';
                        if (isAllowedTile(matrix[currentRow + 1][currentCol])) {
                            currentRow++;
                        } else if (matrix[currentRow + 1][currentCol] == '#') {
                        } else updateDirectionAndPosition();
                    } else updateDirectionAndPosition();

                }
                case UP -> {
                    if (currentRow - 1 >= 0) {
                        matrix[currentRow][currentCol] = '^';
                        if (isAllowedTile(matrix[currentRow - 1][currentCol])) {
                            currentRow--;
                        } else if (matrix[currentRow - 1][currentCol] == '#') {
                        } else updateDirectionAndPosition();
                    } else updateDirectionAndPosition();
                }
            }
        }
    }

    private void movePartOne(int amount, Direction direction) {
        for (int i = 0; i < amount; i++) {
            switch (direction) {
                case RIGHT -> {
                    if (currentCol + 1 < matrix[0].length) {
                        matrix[currentRow][currentCol] = '>';
                        if (isAllowedTile(matrix[currentRow][currentCol + 1])) {
                            currentCol++;
                        } else if (matrix[currentRow][currentCol + 1] == '#') i = amount - 1;
                        else wrapRightToLeftPartOne();
                    } else wrapRightToLeftPartOne();
                }

                case LEFT -> {
                    if (currentCol - 1 >= 0) {
                        matrix[currentRow][currentCol] = '<';
                        if (isAllowedTile(matrix[currentRow][currentCol - 1])) {
                            currentCol--;
                        } else if (matrix[currentRow][currentCol - 1] == '#') i = amount - 1;
                        else wrapLeftToRightPartOne();
                    } else wrapLeftToRightPartOne();
                }

                case DOWN -> {
                    if (currentRow + 1 < matrix.length) { // target is in bounds
                        matrix[currentRow][currentCol] = 'v';
                        if (isAllowedTile(matrix[currentRow + 1][currentCol])) { // target is on the actual field
                            currentRow++;
                        } else if (matrix[currentRow + 1][currentCol] == '#') i = amount - 1;
                        else wrapDownToUpPartOne();
                    } else wrapDownToUpPartOne();
                }

                case UP -> {
                    if (currentRow - 1 >= 0) { // target is in bounds
                        matrix[currentRow][currentCol] = '^';
                        if (isAllowedTile(matrix[currentRow - 1][currentCol])) { // target is on the actual field
                            currentRow--;
                        } else if (matrix[currentRow - 1][currentCol] == '#') i = amount - 1;
                        else wrapUpToDownPartOne();
                    } else wrapUpToDownPartOne();
                }
            }
        }
    }

    private void wrapRightToLeftPartOne() {
        for (int ii = 0; ii < matrix[0].length; ii++) {
            if (matrix[currentRow][ii] == '#') break;
            if (isAllowedTile(matrix[currentRow][ii])) {
                currentCol = ii;
                break;
            }
        }
    }

    private void wrapLeftToRightPartOne() {
        for (int ii = matrix[0].length - 1; ii >= 0; ii--) {
            if (matrix[currentRow][ii] == '#') break;
            if (isAllowedTile(matrix[currentRow][ii])) {
                currentCol = ii;
                break;
            }
        }
    }

    private void wrapUpToDownPartOne() {
        for (int ii = matrix.length - 1; ii >= 0; ii--) {
            if (matrix[ii][currentCol] == '#') break;
            if (isAllowedTile(matrix[ii][currentCol])) {
                currentRow = ii;
                break;
            }
        }
    }

    private void wrapDownToUpPartOne() {
        for (int ii = 0; ii < matrix.length; ii++) {
            if (matrix[ii][currentCol] == '#') break;
            if (isAllowedTile(matrix[ii][currentCol])) {
                currentRow = ii;
                break;
            }
        }
    }

    private boolean isAllowedTile(char c) {
        return c == 'v' || c == '^' || c == '<' || c == '>' || c == '.';
    }

    private Direction getNextDirection(Direction direction, boolean left) {
        Direction result = direction;
        if (left) {
            switch (direction) {
                case UP -> result = Direction.LEFT;
                case LEFT -> result = Direction.DOWN;
                case DOWN -> result = Direction.RIGHT;
                case RIGHT -> result = Direction.UP;
            }
        } else {
            switch (direction) {
                case UP -> result = Direction.RIGHT;
                case RIGHT -> result = Direction.DOWN;
                case DOWN -> result = Direction.LEFT;
                case LEFT -> result = Direction.UP;
            }
        }
        return result;
    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("TwentyTwo")));
            matrix = new char[200][lines.get(0).length()];
            instructions = lines.get(lines.size() - 1);
            currentRow = 0;
            currentCol = lines.get(0).indexOf(".");

            for (int i = 0; i < lines.size() - 2; i++) {
                System.arraycopy(lines.get(i).toCharArray(), 0, matrix[i], 0, lines.get(i).length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

enum Direction {
    RIGHT, DOWN, LEFT, UP
}
