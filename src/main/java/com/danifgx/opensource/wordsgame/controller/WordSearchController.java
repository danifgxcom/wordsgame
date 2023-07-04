package com.danifgx.opensource.wordsgame.controller;

import com.danifgx.opensource.wordsgame.dto.WordSearchResult;
import com.danifgx.opensource.wordsgame.service.WordSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WordSearchController {

    private final WordSearchService wordSearchService;

    public WordSearchController(WordSearchService wordSearchService) {
        this.wordSearchService = wordSearchService;
    }

    @GetMapping("/generate")
    public WordSearchResult generateWordSearch() {
        wordSearchService.generateWordSearch();

        WordSearchResult result = new WordSearchResult();
        result.setMatrix(wordSearchService.getBoard().getMatrix());
        result.setSolutions(wordSearchService.findWordSolutions());

        return result;
    }

}
