package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DayEighteen implements Day {
    private List<Droplet> droplets = new ArrayList<>();
    private List<Droplet> air = new ArrayList<>();
    private int freeSurfaceArea = 0;
    private Set<Droplet> visited = new HashSet<>();
    private int[] bounds = new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE};

    @Override
    public void partOne() {
        readFile();

        for (int i = 0; i < droplets.size(); i++) {
            for (int ii = 0; ii < droplets.size(); ii++) {
                if (i == ii) {
                    continue;
                }

                droplets.get(i).countCoveredSides(droplets.get(ii));
            }
        }

        for (int i = 0; i < droplets.size(); i++) {
            Droplet droplet = droplets.get(i);
            if (droplet.x > bounds[0]) bounds[0] = droplet.x;
            if (droplet.x < bounds[1]) bounds[1] = droplet.x;
            if (droplet.y > bounds[2]) bounds[2] = droplet.y;
            if (droplet.y < bounds[3]) bounds[3] = droplet.y;
            if (droplet.z > bounds[4]) bounds[4] = droplet.z;
            if (droplet.z < bounds[5]) bounds[5] = droplet.z;
            freeSurfaceArea += droplet.freeSides;
        }

        System.out.println(freeSurfaceArea);
    }

    private void flood() {
        Droplet startAir = new Droplet(bounds[1] - 1, bounds[3] - 1, bounds[5] - 1);
        visited.add(startAir);
        visited.addAll(droplets);

        move(startAir.x + 1, startAir.y, startAir.z);
        move(startAir.x, startAir.y + 1, startAir.z);
        move(startAir.x, startAir.y, startAir.z + 1);
    }

    private void move(int x, int y, int z) {
        if (x > bounds[0] + 1 || x < bounds[1] - 1 || y > bounds[2] + 1 || y < bounds[3] - 1 || z > bounds[4] + 1 || z < bounds[5] - 1) {
            return;
        }

        Droplet possibleAirSpace = new Droplet(x, y, z);
        if (!visited.contains(possibleAirSpace)) { // check adjacent lava droplets
            if (droplets.contains(new Droplet(x + 1, y, z))) {
                freeSurfaceArea++;
            }

            if (droplets.contains(new Droplet(x - 1, y, z))) {
                freeSurfaceArea++;
            }

            if (droplets.contains(new Droplet(x, y + 1, z))) {
                freeSurfaceArea++;
            }

            if (droplets.contains(new Droplet(x, y - 1, z))) {
                freeSurfaceArea++;
            }

            if (droplets.contains(new Droplet(x, y, z + 1))) {
                freeSurfaceArea++;
            }

            if (droplets.contains(new Droplet(x, y, z - 1))) {
                freeSurfaceArea++;
            }

            visited.add(possibleAirSpace);
            move(possibleAirSpace.x + 1, possibleAirSpace.y, possibleAirSpace.z);
            move(possibleAirSpace.x - 1, possibleAirSpace.y, possibleAirSpace.z);
            move(possibleAirSpace.x, possibleAirSpace.y + 1, possibleAirSpace.z);
            move(possibleAirSpace.x, possibleAirSpace.y - 1, possibleAirSpace.z);
            move(possibleAirSpace.x, possibleAirSpace.y, possibleAirSpace.z + 1);
            move(possibleAirSpace.x, possibleAirSpace.y, possibleAirSpace.z - 1);
        }
    }


    @Override
    public void partTwo() {
        freeSurfaceArea = 0;
        flood();
        System.out.println(freeSurfaceArea);
    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Eighteen")));

            for (int i = 0; i < lines.size(); i++) {
                String[] coords = lines.get(i).split(",");
                droplets.add(new Droplet(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Droplet {
        private int x;
        private int y;
        private int z;
        private int freeSides;

        public Droplet(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.freeSides = 6;
        }

        public void countCoveredSides(Droplet cube) {
            // y covered
            if (this.x == cube.x && this.z == cube.z && this.y + 1 == cube.y) {
                freeSides--;
            }

            if (this.x == cube.x && this.z == cube.z && this.y - 1 == cube.y) {
                freeSides--;
            }

            // x covered
            if (this.y == cube.y && this.z == cube.z && this.x + 1 == cube.x) {
                freeSides--;
            }

            if (this.y == cube.y && this.z == cube.z && this.x - 1 == cube.x) {
                freeSides--;
            }

            // z covered
            if (this.y == cube.y && this.x == cube.x && this.z + 1 == cube.z) {
                freeSides--;
            }

            if (this.y == cube.y && this.x == cube.x && this.z - 1 == cube.z) {
                freeSides--;
            }

            if (freeSides <= 0) {
                freeSides = 0;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            final Droplet other = (Droplet) obj;
            if (x == other.x && y == other.y && z == other.z) {
                return true;
            }

            return false;
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
            return "[" + x + "," + y + "," + z + "]";
        }
    }
}
