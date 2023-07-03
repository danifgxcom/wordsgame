package com.danifgx.opensource.wordsgame.service;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WordSearchServiceTest {

    private WordSearchService wordSearchService;

    @BeforeEach
    public void setUp() {
        wordSearchService = new WordSearchService();
    }

    @Test
    public void testGenerateWordSearch() {
        JsonNode result = wordSearchService.generateWordSearch();

        // Verificar que se generó la matriz
        JsonNode matrix = result.get("matrix");
        Assertions.assertNotNull(matrix);
        Assertions.assertTrue(matrix.isArray());

        // Verificar que se generaron las soluciones
        JsonNode solutions = result.get("solutions");
        Assertions.assertNotNull(solutions);
        Assertions.assertTrue(solutions.isArray());
    }
}
