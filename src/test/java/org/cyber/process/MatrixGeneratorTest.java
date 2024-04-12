package org.cyber.process;

import org.cyber.model.input.FileConfiguration;
import org.cyber.model.input.Symbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatrixGeneratorTest {

    private MatrixGenerator matrixGenerator;

    @BeforeEach
    public void setUp() {
        matrixGenerator = new MatrixGenerator(false);
    }

    @Test
    public void testGenerateMatrix_includingStandardBonus() {
        // Mock FileConfiguration
        FileConfiguration config = mock(FileConfiguration.class);
        when(config.getRows()).thenReturn(3);
        when(config.getColumns()).thenReturn(3);

        // Mock Symbols
        Map<String, Symbol> symbols = new HashMap<>();
        Symbol standard = new Symbol();
        standard.setType("standard");
        symbols.put("A", standard);
        symbols.put("B", standard);
        symbols.put("C", standard);
        Symbol bonusSymbol = new Symbol();
        bonusSymbol.setType("bonus");
        symbols.put("10x", bonusSymbol);
        symbols.put("MISS", bonusSymbol);
        when(config.getSymbols()).thenReturn(symbols);

        // Generate matrix
        List<List<String>> matrix = matrixGenerator.generateMatrix(config);

        // Verify dimensions
        assertEquals(3, matrix.size());
        assertEquals(3, matrix.get(0).size());

        // Verify symbols
        Set<String> expectedSymbols = new HashSet<>(Arrays.asList("A", "B", "C", "10x", "MISS"));
        for (List<String> row : matrix) {
            for (String symbol : row) {
                assert(expectedSymbols.contains(symbol));
            }
        }
    }

    @Test
    public void testGenerateMatrix_includingOnlyStandard() {
        // Mock FileConfiguration
        FileConfiguration config = mock(FileConfiguration.class);
        when(config.getRows()).thenReturn(3);
        when(config.getColumns()).thenReturn(3);

        // Mock Symbols
        Map<String, Symbol> symbols = new HashMap<>();
        Symbol standard = new Symbol();
        standard.setType("standard");
        symbols.put("A", standard);
        symbols.put("B", standard);
        symbols.put("C", standard);
        when(config.getSymbols()).thenReturn(symbols);

        // Generate matrix
        List<List<String>> matrix = matrixGenerator.generateMatrix(config);

        // Verify dimensions
        assertEquals(3, matrix.size());
        assertEquals(3, matrix.get(0).size());

        // Verify symbols
        Set<String> expectedSymbols = new HashSet<>(Arrays.asList("A", "B", "C"));
        for (List<String> row : matrix) {
            for (String symbol : row) {
                assert(expectedSymbols.contains(symbol));
            }
        }
    }
}