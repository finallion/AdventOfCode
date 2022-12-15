package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DayFifteen implements Day {
    private Map<Position, Position> sensorToBeaconMapping = new HashMap<>();
    private int maxDistance = Integer.MIN_VALUE;
    private int leftXBound = Integer.MAX_VALUE;
    private int rightXBound = Integer.MIN_VALUE;
    private final int RANGE = 4000000;

    @Override
    public void partOne() {
        // THIS COULD NEED A LOT OF REWORK AFTER DOING PART TWO!
        readFile();
        int freeSpaces = 0;

        // bound from lowest reachable to highest reachable point from any sensor
        for (int x = leftXBound - maxDistance; x <= rightXBound + maxDistance; x++) {
            Position test = new Position(x, 2000000);
            boolean isNotBeaconPlacable = false;
            for (Position sensor : sensorToBeaconMapping.keySet()) {
                // if test pos is in range of sensor, it is not placeable
                if (Math.abs(sensor.x - test.x) + Math.abs(sensor.y - test.y) <= sensor.distance) {
                    isNotBeaconPlacable = true;
                    break;
                }
            }

            for (Position beacon : sensorToBeaconMapping.values()) {
                // if test pos is a beacon, it is not placeable
                if (test.x == beacon.x && test.y == beacon.y) {
                    isNotBeaconPlacable = false;
                }
            }

            if (isNotBeaconPlacable) {
                freeSpaces++;
            }

        }

        System.out.println("Result: " + freeSpaces);

    }

    @Override
    public void partTwo() {
        List<Set<Position>> borders = new ArrayList<>();
        for (Position sensor : sensorToBeaconMapping.keySet()) {
            // for each sensor, collect all bordering positions
            Set<Position> sensorBorders = new HashSet<>();

            if (sensor.x + sensor.distance < 0 || sensor.x - sensor.distance > RANGE) {
                continue;
            }

            if (sensor.y + sensor.distance < 0 || sensor.y - sensor.distance > RANGE) {
                continue;
            }


            // left side
            int yUp = sensor.y;
            int yDown = sensor.y;
            for (int x = sensor.x - sensor.distance - 1; x <= sensor.x; x++) {
                if (x < 0 || yUp > RANGE || yDown < 0) { // pos is out of bounds
                    continue;
                }

                Position upBorder = new Position(x, yUp);
                if (sensorBorders.contains(upBorder)) {
                    sensorBorders.remove(upBorder);
                } else {
                    sensorBorders.add(upBorder);
                }

                Position downBorder = new Position(x, yDown);
                if (sensorBorders.contains(downBorder)) {
                    sensorBorders.remove(downBorder);
                } else {
                    sensorBorders.add(downBorder);
                }

                yUp++;
                yDown--;
            }

            // right side
            yUp = sensor.y;
            yDown = sensor.y;
            for (int x = sensor.x + sensor.distance + 1; x > sensor.x; x--) {
                if (x < 0 || yUp > RANGE || yDown < 0) { // pos is out of bounds
                    continue;
                }

                Position upBorder = new Position(x, yUp);
                if (sensorBorders.contains(upBorder)) {
                    sensorBorders.remove(upBorder);
                } else {
                    sensorBorders.add(upBorder);
                }

                Position downBorder = new Position(x, yDown);
                if (sensorBorders.contains(downBorder)) {
                    sensorBorders.remove(downBorder);
                } else {
                    sensorBorders.add(downBorder);
                }

                yUp++;
                yDown--;
            }
            borders.add(sensorBorders);
        }

        // iterate through each position of each set and count how often it occurs in other sets.
        // the distress beacon should be the only field with 4 neighboring signal diamonds (= be in four sets).
        for (Set<Position> set : borders) {
            for (Position pos : set) {
                int hits = 0;
                for (Set<Position> set2 : borders) {
                    if (set2.contains(pos)) {
                        hits++;
                    }
                }

                if (hits >= 4) {
                    System.out.println(pos);
                    long result = (long) pos.x * RANGE + pos.y;
                    System.out.println(result);
                    break;
                }
            }
        }

    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Fifteen")));

            for (int i = 0; i < lines.size(); i++) {
                String[] coords = lines.get(i).split("=");
                Position sensor = new Position(Integer.parseInt(coords[1].split(",")[0]), Integer.parseInt(coords[2].split(":")[0]));
                Position beacon = new Position(Integer.parseInt(coords[3].split(",")[0]), Integer.parseInt(coords[4].split(":")[0]));
                sensor.distance = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
                if (sensor.distance > maxDistance) {
                    maxDistance = sensor.distance;
                }

                if (sensor.x > rightXBound) {
                    rightXBound = sensor.x;
                }

                if (sensor.x < leftXBound) {
                    leftXBound = sensor.x;
                }
                sensorToBeaconMapping.putIfAbsent(sensor, beacon);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class Position {
        int x;
        int y;
        int distance;

        public Position(int x, int y) {
            this(x, y, 0);
        }

        public Position(int x, int y, int distanceToBeacon) {
            this.x = x;
            this.y = y;
            this.distance = distanceToBeacon;
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
            return "[" + x + "/" + y + "]";
        }
    }
}
