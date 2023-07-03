package com.danifgx.opensource.wordsgame.entity;

public class WordSolution {
    private String word;
    private Position startPosition;
    private Position endPosition;

    public WordSolution(String word, Position startPosition, Position endPosition) {
        this.word = word;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public String getWord() {
        return word;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }
}
