package com.danifgx.opensource.wordsgame.service;

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

@RestController
@RequestMapping("/wordsearch")
public class WordSearchService {

    // Lista de palabras (constante, puedes cambiarla para obtenerla de MongoDB)
    private static final String[] WORDS = {"HOLA", "MUNDO", "PRUEBA", "SOPA", "LETRAS"};

    @GetMapping
    public JsonNode generateWordSearch() {
        // Generar la matriz de letras
        char[][] matrix = generateMatrix(10, 10); // Tamaño de la sopa de letras: 10x10

        // Obtener las soluciones (palabra, posición origen y posición destino)
        List<WordSolution> solutions = findSolutions(matrix, WORDS);

        // Crear el JSON de respuesta
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode matrixArray = objectMapper.createArrayNode();
        for (char[] row : matrix) {
            StringBuilder rowBuilder = new StringBuilder();
            for (char letter : row) {
                rowBuilder.append(letter);
            }
            matrixArray.add(rowBuilder.toString());
        }

        ArrayNode solutionsArray = objectMapper.createArrayNode();
        for (WordSolution solution : solutions) {
            ObjectNode solutionObject = objectMapper.createObjectNode();
            solutionObject.put("word", solution.getWord());

            ObjectNode startPositionObject = objectMapper.createObjectNode();
            startPositionObject.put("x", solution.getStartPosition().getX());
            startPositionObject.put("y", solution.getStartPosition().getY());
            solutionObject.set("startPosition", startPositionObject);

            ObjectNode endPositionObject = objectMapper.createObjectNode();
            endPositionObject.put("x", solution.getEndPosition().getX());
            endPositionObject.put("y", solution.getEndPosition().getY());
            solutionObject.set("endPosition", endPositionObject);

            solutionsArray.add(solutionObject);
        }

        ObjectNode result = objectMapper.createObjectNode();
        result.set("matrix", matrixArray);
        result.set("solutions", solutionsArray);

        return result;
    }

    private char[][] generateMatrix(int rows, int cols) {
        char[][] matrix = new char[rows][cols];
        Random random = new Random();

        // Posicionar las palabras aleatoriamente en la matriz
        List<String> wordsToPosition = new ArrayList<>(Arrays.asList(WORDS));

        for (String word : WORDS) {
            boolean positioned = false;

            while (!positioned) {
                Direction.Type direction = Direction.Type.randomDirection();

                int startX = random.nextInt(rows - word.length() + 1);
                int startY = random.nextInt(cols - word.length() + 1);

                if (canPositionWord(matrix, word, direction, startX, startY)) {
                    positionWord(matrix, word, direction, startX, startY);
                    wordsToPosition.remove(word);
                    positioned = true;
                }
            }
        }


        // Completar el resto de la matriz con letras aleatorias
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == '\u0000') {
                    matrix[i][j] = (char) (random.nextInt(26) + 'A'); // Generar una letra aleatoria
                }
            }
        }

        return matrix;
    }

    private boolean canPositionWord(char[][] matrix, String word, Direction.Type direction, int startX, int startY) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int wordLength = word.length();

        int deltaX = direction.getDeltaX();
        int deltaY = direction.getDeltaY();

        int endX = startX + (wordLength - 1) * deltaX;
        int endY = startY + (wordLength - 1) * deltaY;

        return endX >= 0 && endX < cols && endY >= 0 && endY < rows && !hasCollision(matrix, word, startX, startY, deltaX, deltaY);
    }

    private boolean hasCollision(char[][] matrix, String word, int startX, int startY, int deltaX, int deltaY) {
        int wordLength = word.length();

        for (int i = 0; i < wordLength; i++) {
            char currentLetter = matrix[startY + i * deltaY][startX + i * deltaX];
            if (currentLetter != '\u0000' && currentLetter != word.charAt(i)) {
                return true;
            }
        }

        return false;
    }

    private void positionWord(char[][] matrix, String word, Direction.Type direction, int startX, int startY) {
        int deltaX = direction.getDeltaX();
        int deltaY = direction.getDeltaY();

        int wordLength = word.length();

        for (int i = 0; i < wordLength; i++) {
            matrix[startY + i * deltaY][startX + i * deltaX] = word.charAt(i);
        }
    }

    private List<WordSolution> findSolutions(char[][] matrix, String[] words) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        List<WordSolution> solutions = new ArrayList<>();

        for (String word : words) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (matrix[i][j] == word.charAt(0)) {
                        WordSolution solution = findWord(matrix, word, i, j);
                        if (solution != null) {
                            solutions.add(solution);
                        }
                    }
                }
            }
        }

        return solutions;
    }

    private WordSolution findWord(char[][] matrix, String word, int startX, int startY) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int wordLength = word.length();

        for (Direction.Type direction : Direction.Type.values()) {
            if (canPositionWord(matrix, word, direction, startX, startY)) {
                int deltaX = direction.getDeltaX();
                int deltaY = direction.getDeltaY();

                int endX = startX + (wordLength - 1) * deltaX;
                int endY = startY + (wordLength - 1) * deltaY;

                WordSolution ws = new WordSolution(word, new Position(startX, startY), new Position(endX, endY));
                positionWord(matrix, word, direction, startX, startY);
                return ws;
            }
        }

        return null;
    }
}
