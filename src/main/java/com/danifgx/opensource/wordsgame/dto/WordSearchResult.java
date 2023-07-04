package com.danifgx.opensource.wordsgame.dto;

import com.danifgx.opensource.wordsgame.entity.WordSolution;
import lombok.Data;

import java.util.List;

@Data
public class WordSearchResult {
    private List<String> matrix;
    private List<WordSolution> solutions;
}