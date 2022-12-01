package com.finallion;

public interface Day {

    void partOne();

    void partTwo();

    default String buildPath(String day) {
        return "src/com/finallion/inputs/day" + day + "Input.txt";
    }

}
