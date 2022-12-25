package com.finallion.util;

import com.finallion.DayTwentyFour;

import java.util.Objects;

public final class Position implements Comparable<Position> {

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    private final int row;
    private final int col;

    private Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isValid(int maxRow, int maxCol) {
        return this.row >= 0 && this.col >= 0 && this.row < maxRow && this.col < maxCol;
    }

    public static Position of(int i, int j) {
        return new Position(i, j);
    }

    public Position moveLeft() {
        return new Position(row, col - 1);
    }

    public Position moveRight() {
        return new Position(row, col + 1);
    }

    public Position moveUp() {
        return new Position(row - 1, col);
    }

    public Position moveDown() {
        return new Position(row + 1, col);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        return another instanceof Position && equalTo((Position) another);
    }

    private boolean equalTo(Position another) {
        return Objects.equals(row, another.row) && Objects.equals(col, another.col);
    }

    @Override
    public int compareTo(Position p) {
        int value = this.row - p.row;
        if (value == 0) {
            value = this.col - p.col;
        }
        return value;
    }
}
