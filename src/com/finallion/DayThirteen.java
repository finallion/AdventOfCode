package com.finallion;

import java.io.File;
import java.util.*;

/* Thanks to https://github.com/abnew123/aoc2022/blob/main/src/aoc2022/Day13.java */
public class DayThirteen implements Day {

    @Override
    public void partOne() {
        System.out.println("Result: " + readFile(true));
    }

    @Override
    public void partTwo() {
        System.out.println("Result: " + readFile(false));
    }


    public int readFile(boolean partOne) {
        File file = new File(this.buildPath("Thirteen"));

        int sumIndexes = 0;
        int index = 1;
        List<Packet> packets = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                Packet left = new Packet(scanner.nextLine());
                Packet right = new Packet(scanner.nextLine());

                if (scanner.hasNext()) {
                    scanner.nextLine();
                }

                if (partOne) {
                    if (left.compareTo(right) > 0) {
                        sumIndexes += index;
                    }

                    index++;
                } else {
                    packets.add(left);
                    packets.add(right);
                }
            }

            if (!partOne) {
                sumIndexes = 1;
                packets.add(new Packet("[[2]]"));
                packets.add(new Packet("[[6]]"));
                Collections.sort(packets);
                Collections.reverse(packets);
                for (int i = 0; i < packets.size(); i++) {
                    if (packets.get(i).packetContent.equals("[[2]]") || packets.get(i).packetContent.equals("[[6]]")) {
                        sumIndexes *= (i + 1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sumIndexes;
    }

    class Packet implements Comparable<Packet> {
        private List<Packet> innerPackets;
        private int value;
        private boolean isIntegerPacket;
        private String packetContent;

        public Packet(String packetContent) {
            this.packetContent = packetContent;
            innerPackets = new ArrayList<>();

            if (packetContent.equals("[]")) {
                value = -1; // arbitrary negative value
            }

            if (!packetContent.startsWith("[")) { // int paket
                isIntegerPacket = true;
                value = Integer.parseInt(packetContent);
            } else { // list paket
                isIntegerPacket = false;

                packetContent = packetContent.substring(1, packetContent.length() - 1);

                // build sub packets
                int level = 0; // keep track of inner packets
                String innerPacketContent = "";
                for (Character character : packetContent.toCharArray()) {
                    if (character == ',' && level == 0) { // ex: [],[] or [[]],[] ... marks split between two separate inner packets
                        innerPackets.add(new Packet(innerPacketContent));
                        innerPacketContent = "";
                    } else {
                        if (character == '[') {
                            level++;
                        } else if (character == ']') {
                            level--;
                        }

                        innerPacketContent += character;
                    }
                }

                // add sub packet as child
                if (!innerPacketContent.equals("")) {
                    innerPackets.add(new Packet(innerPacketContent));
                }
            }
        }

        public int compareTo(Packet other) {
            // both packets are int packets
            if (isIntegerPacket && other.isIntegerPacket) {
                return other.value - value; // if left packet is greater, return negative value -> out of order
            }

            // both packets are lists
            if (!isIntegerPacket && !other.isIntegerPacket) {
                for (int i = 0; i < Math.min(innerPackets.size(), other.innerPackets.size()); i++) {
                    int val = innerPackets.get(i).compareTo(other.innerPackets.get(i));

                    if (val != 0) { // left is smaller -> in order, return difference
                        return val;
                    }
                }
                // if no value was smaller, check if one packet is larger than the other
                // negative if right packet ran out -> out of order
                return other.innerPackets.size() - innerPackets.size();
            }

            // mixed, convert the int one to a list packet
            if (isIntegerPacket) {
                return new Packet("[" + value + "]").compareTo(other);
            } else {
                return this.compareTo(new Packet("[" + other.value + "]"));
            }
        }
    }
}
