package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DayEight implements Day {
    private int[][] matrix;

    @Override
    public void partOne() {
        readFile();

        List<Position> positions = new ArrayList<>();
        for (int i = 1; i < matrix[0].length - 1; i++) {
            for (int ii = 1; ii < matrix.length - 1; ii++) {
                int tree = matrix[i][ii];

                if (checkUp(tree, i, ii) && checkDown(tree, i, ii) && checkRight(tree, i, ii) && checkLeft(tree, i, ii)) {
                    positions.add(new Position(i, ii, tree));
                }
            }
        }

        System.out.println("Visible trees: " + (matrix[0].length * matrix.length - positions.size()) + ".");
    }

    @Override
    public void partTwo() {
        int maxScore = 0;
        for (int i = 0; i < matrix[0].length; i++) {
            for (int ii = 0; ii < matrix.length; ii++) {
                int tree = matrix[i][ii];
                int scenicScore = checkUpUntilBlocked(tree, i, ii) * checkLeftUntilBlocked(tree, i, ii) * checkRightUntilBlocked(tree, i, ii) * checkDownUntilBlocked(tree, i, ii);
                if (scenicScore > maxScore) {
                    maxScore = scenicScore;
                }
            }
        }

        System.out.println("Heighest score: " + maxScore + ".");
    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Eight")));
            matrix = new int[lines.get(0).length()][lines.size()];

            for (int i = 0; i < lines.size(); i++) {
                String[] numbers = lines.get(i).split("");
                for (int ii = 0; ii < numbers.length; ii++) {
                    matrix[i][ii] = Integer.parseInt(numbers[ii]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int checkUpUntilBlocked(int tree, int i, int ii) {
        int score = 0;
        for (int iii = i - 1; iii >= 0; iii--) {
            if (matrix[iii][ii] < tree) {
                score++;
            } else if (matrix[iii][ii] >= tree) {
                score++;
                break;
            }
        }
        return score;
    }

    private int checkDownUntilBlocked(int tree, int i, int ii) {
        int score = 0;
        for (int iii = i + 1; iii < matrix.length; iii++) {
            if (matrix[iii][ii] < tree) {
                score++;
            } else if (matrix[iii][ii] >= tree) {
                score++;
                break;
            }
        }
        return score;
    }

    private int checkLeftUntilBlocked(int tree, int i, int ii) {
        int score = 0;
        for (int iii = ii - 1; iii >= 0; iii--) {
            if (matrix[i][iii] < tree) {
                score++;
            } else if (matrix[i][iii] >= tree) {
                score++;
                break;
            }
        }
        return score;
    }

    private int checkRightUntilBlocked(int tree, int i, int ii) {
        int score = 0;
        for (int iii = ii + 1; iii < matrix[0].length; iii++) {
            if (matrix[i][iii] < tree) {
                score++;
            } else if (matrix[i][iii] >= tree) {
                score++;
                break;
            }
        }
        return score;
    }

    private boolean checkUp(int tree, int i, int ii) {
        for (int iii = 0; iii < i; iii++) {
            if (matrix[iii][ii] >= tree) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDown(int tree, int i, int ii) {
        for (int iii = i + 1; iii < matrix[0].length; iii++) {
            if (matrix[iii][ii] >= tree) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLeft(int tree, int i, int ii) {
        for (int iii = 0; iii < ii; iii++) {
            if (matrix[i][iii] >= tree) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRight(int tree, int i, int ii) {
        for (int iii = ii + 1; iii < matrix.length; iii++) {
            if (matrix[i][iii] >= tree) {
                return true;
            }
        }
        return false;
    }

    static class Position {
        int row;
        int col;
        int value;

        public Position(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + row + "," + col + "]: " + value;
        }
    }
}
