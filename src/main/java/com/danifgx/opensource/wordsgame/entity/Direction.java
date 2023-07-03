package com.danifgx.opensource.wordsgame.entity;

import java.util.Random;

public class Direction {
    public enum Type {
        HORIZONTAL(1, 0),
        VERTICAL(0, 1),
        HORIZONTAL_REVERSE(-1, 0),
        VERTICAL_REVERSE(0, -1),
        DIAGONAL_DESCENDING(1, 1),
        DIAGONAL_ASCENDING(-1, 1),
        DIAGONAL_DESCENDING_REVERSE(-1, -1),
        DIAGONAL_ASCENDING_REVERSE(1, -1);

        private final int dx;
        private final int dy;

        Type(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public int getDeltaX() {
            return dx;
        }

        public int getDeltaY() {
            return dy;
        }

        public static Type randomDirection() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }
}
