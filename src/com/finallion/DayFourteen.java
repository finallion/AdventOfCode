package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class DayFourteen implements Day {
    private char[][] cave = new char[500][750];
    private int sandCounter = 0;
    private int[] bounds = {Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE};
    boolean sandFall = true;

    @Override
    public void partOne() {
        /*
        readFile(true);
        printCaveSystem();

        while (sandFall) {
            new Sand(0, 500);
        }
        printCaveSystem();
        System.out.println("Result: " + sandCounter);

         */
    }

    @Override
    public void partTwo() {
        readFile(false);
        printCaveSystem();

        while (sandFall) {
            new Sand(0, 500);

            if (cave[0][500] == 'o') {
                sandFall = false;
            }
        }
        System.out.println("---------------");
        printCaveSystem();
        System.out.println("Result: " + sandCounter);

    }

    public void readFile(boolean partOne) {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Fourteen")));

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] coords = line.split(" -> ");

                for (int ii = 0; ii < coords.length - 1; ii++) {
                    int startY = Integer.parseInt(coords[ii].split(",")[0]);
                    int startX = Integer.parseInt(coords[ii].split(",")[1]);
                    int endY = Integer.parseInt(coords[ii + 1].split(",")[0]);
                    int endX = Integer.parseInt(coords[ii + 1].split(",")[1]);

                    if (Math.max(startX, endX) > bounds[0]) bounds[0] = Math.max(startX, endX);
                    if (Math.min(startY, endY) < bounds[1]) bounds[1] = Math.min(startY, endY);
                    if (Math.max(startY, endY) > bounds[2]) bounds[2] = Math.max(startY, endY);

                    for (int iii = Math.min(startX, endX); iii <= Math.max(startX, endX); iii++) {
                        for (int iiii = Math.min(startY, endY); iiii <= Math.max(startY, endY); iiii++) {
                            cave[iii][iiii] = '#';
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!partOne) {
            bounds[0] += 2;
        }

    }

    private void printCaveSystem() {
        for (int i = 0; i <= bounds[0]; i++) {
            for (int ii = bounds[1]; ii <= bounds[2]; ii++) {
                System.out.print(cave[i][ii] + " ");
            }
            System.out.println();
        }
    }

    class Sand {
        int posRow;
        int posCol;

        public Sand(int x, int y) {
            posRow = x;
            posCol = y;

            move(x, y);
        }

        private boolean inBounds(int x, int y) {

            if (x < 0 || x >= bounds[0]) {
                return false;
            }

            /* part one
            if (x < 0 || x >= bounds[0] + 1 || y < bounds[1] - 1 || y >= bounds[2] + 1) {
                return false;
            }

             */

            return true;
        }

        private boolean fallDown(int x, int y) {
            if (inBounds(x + 1, y)) {
                if (free(x + 1, y)) {
                    move(x + 1, y);
                    return true;
                }
            }
            return false;
        }

        private boolean fallLeftDiagonal(int x, int y) {
            if (inBounds(x + 1, y - 1)) {
                if (free(x + 1, y - 1)) {
                    move(x + 1, y - 1);
                    return true;
                }
            }
            return false;
        }

        private boolean fallRightDiagonal(int x, int y) {
            if (inBounds(x + 1, y + 1)) {
                if (free(x + 1, y + 1)) {
                    move(x + 1, y + 1);
                    return true;
                }
            }
            return false;
        }

        private boolean free(int x, int y) {
            if (cave[x][y] != '#' && cave[x][y] != 'o') {
                return true;
            }
            return false;
        }

        private void move(int x, int y) {
            if (sandFall) {
                if (fallDown(x, y)) {

                } else if (fallLeftDiagonal(x, y)) {

                } else if (fallRightDiagonal(x, y)) {

                } else {
                    if (free(x, y)) {
                        cave[x][y] = 'o';
                        sandCounter++;
                    }

                }
            }
        }

    }
}
