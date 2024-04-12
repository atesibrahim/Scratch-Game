package org.cyber.process;

import org.cyber.model.input.FileConfiguration;
import org.cyber.model.input.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.cyber.constant.Constants.BONUS;

public class MatrixGenerator {
    private final List<String> standardSymbolList;
    private final List<String> bonusSymbolList;
    private boolean isAddedBonusSymbolBefore;

    public MatrixGenerator(boolean isAddedBonusSymbolBefore) {
        this.isAddedBonusSymbolBefore = isAddedBonusSymbolBefore;
        this.standardSymbolList = new ArrayList<>();
        this.bonusSymbolList = new ArrayList<>();
    }

    public List<List<String>> generateMatrix(FileConfiguration config) {
        Random rand = new Random();
        addSymbols(config);

        return IntStream.range(0, config.getRows())
                .mapToObj(i -> generateRow(config.getColumns(), rand))
                .collect(Collectors.toList());
    }

    private void addSymbols(FileConfiguration config) {
        for (Map.Entry<String, Symbol> entry : config.getSymbols().entrySet()) {
            String symbol = entry.getKey();
            Symbol symbolConfig = entry.getValue();

            standardSymbolList.add(symbol);
            if (symbolConfig.getType().equals(BONUS)) {
                bonusSymbolList.add(symbol);
            }
        }
    }

    private List<String> generateRow(int columns, Random rand) {
        return IntStream.range(0, columns)
                .mapToObj(j -> getRandomSymbol(rand))
                .collect(Collectors.toList());
    }

    private String getRandomSymbol(Random rand) {
        String symbol = standardSymbolList.get(rand.nextInt(standardSymbolList.size()));
        if (bonusSymbolList.contains(symbol)) {
            if(isAddedBonusSymbolBefore) {
                return getRandomSymbol(rand);
            } else {
                isAddedBonusSymbolBefore = true;
                return symbol;
            }
        }
        return symbol;
    }
}
