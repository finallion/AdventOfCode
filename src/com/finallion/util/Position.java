package com.finallion.util;

import java.util.List;
import java.util.Objects;

public class Position<E> {
    private int x;
    private int y;
    private E data;
    int[][] offsets = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {0, 1}, {1,1}};

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.data = null;
    }

    public Position(int x, int y, E data) {
        this.x = x;
        this.y = y;
        this.data = data;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position<?> position = (Position<?>) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
