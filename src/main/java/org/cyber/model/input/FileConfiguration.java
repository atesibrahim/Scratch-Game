package org.cyber.model.input;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class FileConfiguration {
    private int columns;
    private int rows;
    private Map<String, Symbol> symbols;
    private Probabilities probabilities;
    private Map<String, WinCombination> win_combinations;
}
