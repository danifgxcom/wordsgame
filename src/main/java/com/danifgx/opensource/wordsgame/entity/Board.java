package com.danifgx.opensource.wordsgame.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private char[][] board;
    private int rows;
    private int cols;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new char[rows][cols];
    }

    public void setPosition(int row, int col, char value) {
        board[row][col] = value;
    }

    public char getPosition(int row, int col) {
        return board[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean isPositionEmpty(int row, int col) {
        return getPosition(row, col) == '\u0000';
    }

    public Position getRandomPosition() {
        Random random = new Random();
        int x = random.nextInt(rows);
        int y = random.nextInt(cols);
        return new Position(x, y);
    }

    public List<String> getMatrix() {
        List<String> matrix = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            StringBuilder rowString = new StringBuilder();
            for (int col = 0; col < cols; col++) {
                rowString.append(board[row][col]);
            }
            matrix.add(rowString.toString());
        }
        return matrix;
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(this.board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
