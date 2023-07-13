package com.scnu.controller;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public static int getHorizontalSign(Direction d) {
        if (d == LEFT)
            return -1;
        if (d == RIGHT)
            return 1;
        return 0;
    }

    public static int getVerticalSign(Direction d) {
        if (d == UP)
            return -1;
        if (d == DOWN)
            return 1;
        return 0;
    }
}
