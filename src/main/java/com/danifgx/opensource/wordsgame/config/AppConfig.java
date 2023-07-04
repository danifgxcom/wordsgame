package com.danifgx.opensource.wordsgame.config;

import com.danifgx.opensource.wordsgame.service.WordSearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {

    @Value("${wordsearch.words}")
    private List<String> words;

    @Value("${wordsearch.rows}")
    private int rows;

    @Value("${wordsearch.cols}")
    private int cols;

    @Bean
    public List<String> words() {
        return words;
    }

    @Bean
    public int rows() {
        return rows;
    }

    @Bean
    public int cols() {
        return cols;
    }
    @Bean
    public WordSearchService wordSearchService(List<String> words, int rows, int cols) {
        return new WordSearchService(rows, cols, words);
    }

}
