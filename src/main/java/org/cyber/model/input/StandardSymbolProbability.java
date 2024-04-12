package org.cyber.model.input;

import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class StandardSymbolProbability implements Serializable {
    private int column;
    private int row;
    private Map<String, Double> symbols;
}
