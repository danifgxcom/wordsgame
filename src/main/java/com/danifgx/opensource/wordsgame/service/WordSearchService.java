package com.danifgx.opensource.wordsgame.service;

import com.danifgx.opensource.wordsgame.entity.Board;
import com.danifgx.opensource.wordsgame.entity.Direction;
import com.danifgx.opensource.wordsgame.entity.Position;
import com.danifgx.opensource.wordsgame.entity.WordSolution;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
public class WordSearchService {
    private List<String> words;
    private Board board;

    public WordSearchService(int rows, int cols, List<String> words) {
        this.words = words;
        this.board = new Board(rows, cols);
    }
    public void generateWordSearch() {
        // Intenta colocar todas las palabras en el tablero
        boolean allWordsPositioned = tryPositionAllWords();

        // Si no se pudieron colocar todas las palabras, genera una matriz vacía
        if (!allWordsPositioned) {
            board = new Board(board.getRows(), board.getCols());
        }

        // Completar el resto de la matriz con letras aleatorias
        fillEmptyCells();

        // Imprimir el resultado
        printWordSearch();
    }

    private int getRandomCoordinate(int maxCoordinate) {
        Random random = new Random();
        return random.nextInt(maxCoordinate);
    }

    private boolean tryPositionAllWords() {
        List<String> wordsToPosition = new ArrayList<>(words);
        int totalAttempts = 0;
        boolean allWordsPositioned = false;

        while (!allWordsPositioned && totalAttempts < 50) {
            board = new Board(board.getRows(), board.getCols());
            int attempts = 0;

            for (String word : words) {
                boolean positioned = false;

                while (!positioned && attempts < 50) {
                    Direction.Type direction = Direction.Type.randomDirection();
                    int startX = getRandomCoordinate(board.getCols() - word.length() + 1);
                    int startY = getRandomCoordinate(board.getRows() - word.length() + 1);

                    if (canPositionWord(word, direction, startX, startY)) {
                        positionWord(word, direction, startX, startY);
                        wordsToPosition.remove(word);
                        positioned = true;
                    }

                    attempts++;
                }

                if (!positioned) {
                    // No se pudo colocar la palabra después de 50 intentos, reiniciar el proceso
                    wordsToPosition.addAll(words);
                    board = new Board(board.getRows(), board.getCols());
                    totalAttempts++;
                    break;
                }
            }

            if (wordsToPosition.isEmpty()) {
                // Todas las palabras se han posicionado correctamente
                allWordsPositioned = true;
            }
        }

        return allWordsPositioned;
    }

    private boolean canPositionWord(String word, Direction.Type direction, int startX, int startY) {
        int deltaX = direction.getDeltaX();
        int deltaY = direction.getDeltaY();
        int wordLength = word.length();

        int endX = startX + (wordLength - 1) * deltaX;
        int endY = startY + (wordLength - 1) * deltaY;

        if (!board.isValidPosition(endY, endX)) {
            return false;
        }

        for (int i = 0; i < wordLength; i++) {
            int currentX = startX + i * deltaX;
            int currentY = startY + i * deltaY;

            if (!board.isValidPosition(currentY, currentX) || (!board.isPositionEmpty(currentY, currentX) && board.getPosition(currentY, currentX) != word.charAt(i))) {
                return false;
            }
        }

        return true;
    }



    private void positionWord(String word, Direction.Type direction, int startX, int startY) {
        int deltaX = direction.getDeltaX();
        int deltaY = direction.getDeltaY();

        for (int i = 0; i < word.length(); i++) {
            int row = startY + i * deltaY;
            int col = startX + i * deltaX;
            board.setPosition(row, col, word.charAt(i));
        }
    }

    private void fillEmptyCells() {
        Random random = new Random();

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                if (board.isPositionEmpty(row, col)) {
                    board.setPosition(row, col, (char) (random.nextInt(26) + 'A'));
                }
            }
        }
    }

    private void printWordSearch() {
        System.out.println("Matrix:");
        board.print();

        System.out.println("Solutions:");
        List<WordSolution> solutions = findWordSolutions();
        for (WordSolution solution : solutions) {
            System.out.println(solution);
        }
    }

    public  List<WordSolution> findWordSolutions() {
        List<WordSolution> solutions = new ArrayList<>();

        for (String word : words) {
            WordSolution solution = findWordSolution(word);
            if (solution != null) {
                solutions.add(solution);
            }
        }

        return solutions;
    }

    private WordSolution findWordSolution(String word) {
        Direction.Type[] directions = Direction.Type.values();

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                for (Direction.Type direction : directions) {
                    int deltaX = direction.getDeltaX();
                    int deltaY = direction.getDeltaY();

                    int endRow = row + (word.length() - 1) * deltaY;
                    int endCol = col + (word.length() - 1) * deltaX;

                    if (board.isValidPosition(endRow, endCol)) {
                        boolean found = true;
                        for (int i = 0; i < word.length(); i++) {
                            int currentRow = row + i * deltaY;
                            int currentCol = col + i * deltaX;
                            if (board.getPosition(currentRow, currentCol) != word.charAt(i)) {
                                found = false;
                                break;
                            }
                        }
                        if (found) {
                            return new WordSolution(word, new Position(col, row), new Position(endCol, endRow));
                        }
                    }
                }
            }
        }

        return null;
    }


    public Board getBoard() {
        return board;
    }
}