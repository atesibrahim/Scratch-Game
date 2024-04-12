package org.cyber.process;

import org.cyber.model.input.BonusSymbols;
import org.cyber.model.input.FileConfiguration;
import org.cyber.model.input.Probabilities;
import org.cyber.model.input.Symbol;
import org.cyber.model.input.WinCombination;
import org.cyber.model.output.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.cyber.constant.Constants.SAME_SYMBOLS_DIAGONALLY_LEFT_TO_RIGHT;
import static org.cyber.constant.Constants.SAME_SYMBOLS_DIAGONALLY_RIGHT_TO_LEFT;
import static org.cyber.constant.Constants.SAME_SYMBOLS_HORIZONTALLY;
import static org.cyber.constant.Constants.SAME_SYMBOLS_VERTICALLY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameLogicImplementationTest {

    private GameLogicImplementation gameLogic;

    @BeforeEach
    public void setUp() {
        Output output = Output.builder().build();
        gameLogic = new GameLogicImplementation(output);
    }

    @Test
    public void testGetOutput_calculatesReward_when_have_times() {
        // Mock FileConfiguration
        FileConfiguration config = mock(FileConfiguration.class);
        Map<String, Symbol> symbols = getStringSymbolMap();
        when(config.getSymbols()).thenReturn(symbols);

        // Mock WinCombinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        WinCombination winCombination = new WinCombination();
        winCombination.setReward_multiplier(2.0);
        winCombination.setCount(3);
        winCombinations.put("3_times", winCombination);
        WinCombination winCombination2 = new WinCombination();
        winCombination2.setReward_multiplier(3.0);
        winCombination2.setCount(4);
        winCombinations.put("4_times", winCombination2);
        when(config.getWin_combinations()).thenReturn(winCombinations);

        // Mock WinCombinations


        // Mock matrix
        List<List<String>> matrix = new ArrayList<>();
        matrix.add(Arrays.asList("A", "A", "A"));
        matrix.add(Arrays.asList("B", "B", "C"));
        matrix.add(Arrays.asList("C", "C", "C"));

        // Act
        Output actualOutput = gameLogic.getOutput(matrix, config, 1);

        // Verify
        assertEquals(2.0 * 1 + 3.0 * 3 * 1, actualOutput.getReward(), 0.001);
    }

    @Test
    public void testGetOutput_calculatesReward_when_verticallySame() {
        // Mock FileConfiguration
        FileConfiguration config = mock(FileConfiguration.class);
        Map<String, Symbol> symbols = getStringSymbolMap();
        when(config.getSymbols()).thenReturn(symbols);

        // Mock WinCombinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        WinCombination winCombination = new WinCombination();
        winCombination.setReward_multiplier(25.0);
        winCombination.setCount(3);
        winCombinations.put(SAME_SYMBOLS_VERTICALLY, winCombination);
        WinCombination winCombination2 = new WinCombination();
        winCombination2.setReward_multiplier(2.0);
        winCombination2.setCount(3);
        winCombinations.put("3_times", winCombination2);
        when(config.getWin_combinations()).thenReturn(winCombinations);

        // Mock WinCombinations


        // Mock matrix
        List<List<String>> matrix = new ArrayList<>();
        matrix.add(Arrays.asList("A", "D", "E"));
        matrix.add(Arrays.asList("A", "B", "C"));
        matrix.add(Arrays.asList("A", "F", "C"));

        // Act
        Output actualOutput = gameLogic.getOutput(matrix, config, 1);

        // Verify
        assertEquals(2.0 * 1 * 25.0 * 1 , actualOutput.getReward(), 0.001);
    }

    @Test
    public void testGetOutput_calculatesReward_when_horizontallySame() {
        // Mock FileConfiguration
        FileConfiguration config = mock(FileConfiguration.class);
        Map<String, Symbol> symbols = getStringSymbolMap();
        when(config.getSymbols()).thenReturn(symbols);

        // Mock WinCombinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        WinCombination winCombination = new WinCombination();
        winCombination.setReward_multiplier(40.0);
        winCombination.setCount(3);
        winCombinations.put(SAME_SYMBOLS_HORIZONTALLY, winCombination);
        WinCombination winCombination2 = new WinCombination();
        winCombination2.setReward_multiplier(2.0);
        winCombination2.setCount(4);
        winCombinations.put("4_times", winCombination2);
        when(config.getWin_combinations()).thenReturn(winCombinations);

        // Mock WinCombinations


        // Mock matrix
        List<List<String>> matrix = new ArrayList<>();
        matrix.add(Arrays.asList("A", "A", "A"));
        matrix.add(Arrays.asList("A", "B", "C"));
        matrix.add(Arrays.asList("B", "E", "D"));

        // Act
        Output actualOutput = gameLogic.getOutput(matrix, config, 1);

        // Verify
        assertEquals(2.0 * 1 * 40.0 * 1 , actualOutput.getReward(), 0.001);
    }

    @Test
    public void testGetOutput_calculatesReward_when_leftDiagonallySame() {
        // Mock FileConfiguration
        FileConfiguration config = mock(FileConfiguration.class);
        Map<String, Symbol> symbols = getStringSymbolMap();
        when(config.getSymbols()).thenReturn(symbols);

        // Mock WinCombinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        WinCombination winCombination = new WinCombination();
        winCombination.setReward_multiplier(30.0);
        winCombination.setCount(3);
        winCombinations.put(SAME_SYMBOLS_DIAGONALLY_LEFT_TO_RIGHT, winCombination);
        WinCombination winCombination2 = new WinCombination();
        winCombination2.setReward_multiplier(2.0);
        winCombination2.setCount(4);
        winCombinations.put("4_times", winCombination2);
        when(config.getWin_combinations()).thenReturn(winCombinations);

        // Mock WinCombinations


        // Mock matrix
        List<List<String>> matrix = new ArrayList<>();
        matrix.add(Arrays.asList("A", "D", "A"));
        matrix.add(Arrays.asList("F", "A", "C"));
        matrix.add(Arrays.asList("B", "E", "A"));

        // Act
        Output actualOutput = gameLogic.getOutput(matrix, config, 1);

        // Verify
        assertEquals(2.0 * 1 * 30.0 * 1 , actualOutput.getReward(), 0.001);
    }

    @Test
    public void testGetOutput_calculatesReward_when_rightDiagonallySame() {
        // Mock FileConfiguration
        FileConfiguration config = mock(FileConfiguration.class);
        Map<String, Symbol> symbols = getStringSymbolMap();
        when(config.getSymbols()).thenReturn(symbols);

        // Mock WinCombinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        WinCombination winCombination = new WinCombination();
        winCombination.setReward_multiplier(10.0);
        winCombination.setCount(3);
        winCombinations.put(SAME_SYMBOLS_DIAGONALLY_RIGHT_TO_LEFT, winCombination);
        WinCombination winCombination2 = new WinCombination();
        winCombination2.setReward_multiplier(2.0);
        winCombination2.setCount(3);
        winCombinations.put("3_times", winCombination2);
        when(config.getWin_combinations()).thenReturn(winCombinations);

        // Mock WinCombinations


        // Mock matrix
        List<List<String>> matrix = new ArrayList<>();
        matrix.add(Arrays.asList("A", "D", "B"));
        matrix.add(Arrays.asList("F", "B", "C"));
        matrix.add(Arrays.asList("B", "E", "A"));

        // Act
        Output actualOutput = gameLogic.getOutput(matrix, config, 1);

        // Verify
        assertEquals(2.0 * 2 * 10.0 * 1 , actualOutput.getReward(), 0.001);
    }

    @Test
    public void testGetOutput_calculatesReward_when_haveMultiplierBonus() {
        // Mock FileConfiguration
        FileConfiguration config = mock(FileConfiguration.class);
        Map<String, Symbol> symbols = getStringSymbolMap();
        when(config.getSymbols()).thenReturn(symbols);

        BonusSymbols bonusSymbols = new BonusSymbols();
        bonusSymbols.setSymbols(Map.of("5x", 5.0));
        Probabilities probabilities = new Probabilities();
        probabilities.setBonus_symbols(bonusSymbols);
        when(config.getProbabilities()).thenReturn(probabilities);

        // Mock WinCombinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        WinCombination winCombination = new WinCombination();
        winCombination.setReward_multiplier(10.0);
        winCombination.setCount(3);
        winCombinations.put(SAME_SYMBOLS_DIAGONALLY_RIGHT_TO_LEFT, winCombination);
        WinCombination winCombination2 = new WinCombination();
        winCombination2.setReward_multiplier(2.0);
        winCombination2.setCount(3);
        winCombinations.put("3_times", winCombination2);
        when(config.getWin_combinations()).thenReturn(winCombinations);

        // Mock WinCombinations


        // Mock matrix
        List<List<String>> matrix = new ArrayList<>();
        matrix.add(Arrays.asList("A", "D", "B"));
        matrix.add(Arrays.asList("F", "5x", "B"));
        matrix.add(Arrays.asList("B", "E", "A"));

        // Act
        Output actualOutput = gameLogic.getOutput(matrix, config, 1);

        // Verify
        assertEquals(2.0 * 2 * 5.0 * 1 , actualOutput.getReward(), 0.001);
    }

    @Test
    public void testGetOutput_calculatesReward_when_haveSumBonus() {
        // Mock FileConfiguration
        FileConfiguration config = mock(FileConfiguration.class);
        Map<String, Symbol> symbols = getStringSymbolMap();
        when(config.getSymbols()).thenReturn(symbols);

        BonusSymbols bonusSymbols = new BonusSymbols();
        bonusSymbols.setSymbols(Map.of("+100", 1.0));
        Probabilities probabilities = new Probabilities();
        probabilities.setBonus_symbols(bonusSymbols);
        when(config.getProbabilities()).thenReturn(probabilities);

        // Mock WinCombinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        WinCombination winCombination = new WinCombination();
        winCombination.setReward_multiplier(10.0);
        winCombination.setCount(3);
        winCombinations.put(SAME_SYMBOLS_DIAGONALLY_RIGHT_TO_LEFT, winCombination);
        WinCombination winCombination2 = new WinCombination();
        winCombination2.setReward_multiplier(2.0);
        winCombination2.setCount(3);
        winCombinations.put("3_times", winCombination2);
        when(config.getWin_combinations()).thenReturn(winCombinations);

        // Mock WinCombinations


        // Mock matrix
        List<List<String>> matrix = new ArrayList<>();
        matrix.add(Arrays.asList("A", "D", "B"));
        matrix.add(Arrays.asList("F", "+100", "B"));
        matrix.add(Arrays.asList("B", "E", "A"));

        // Act
        Output actualOutput = gameLogic.getOutput(matrix, config, 1);

        // Verify
        assertEquals(2.0 * 2 * 1 + 100 , actualOutput.getReward(), 0.001);
    }

    @Test
    public void testGetOutput_returnsZero_when_configNull() {
        // Mock matrix
        List<List<String>> matrix = new ArrayList<>();
        matrix.add(Arrays.asList("A", "D", "E"));
        matrix.add(Arrays.asList("A", "B", "C"));
        matrix.add(Arrays.asList("A", "F", "C"));

        // Act
        Output actualOutput = gameLogic.getOutput(matrix, null, 1);

        // Verify
        assertEquals(0, actualOutput.getReward(), 0.001);
    }

    @Test
    public void testGetOutput_returnsZero_when_winCombinationNull() {
        // Mock FileConfiguration
        FileConfiguration config = mock(FileConfiguration.class);
        Map<String, Symbol> symbols = getStringSymbolMap();
        when(config.getSymbols()).thenReturn(symbols);

        // Mock matrix
        List<List<String>> matrix = new ArrayList<>();

        // Act
        Output actualOutput = gameLogic.getOutput(matrix, config, 1);

        // Verify
        assertEquals(0, actualOutput.getReward(), 0.001);
    }

    @Test
    public void testGetOutput_returnsZero_when_matrixNull() {
        // Mock FileConfiguration
        FileConfiguration config = mock(FileConfiguration.class);
        Map<String, Symbol> symbols = getStringSymbolMap();
        when(config.getSymbols()).thenReturn(symbols);

        // Mock WinCombinations
        Map<String, WinCombination> winCombinations = new HashMap<>();
        WinCombination winCombination = new WinCombination();
        winCombination.setReward_multiplier(2.0);
        winCombination.setCount(3);
        winCombinations.put("3_times", winCombination);
        WinCombination winCombination2 = new WinCombination();
        winCombination2.setReward_multiplier(3.0);
        winCombination2.setCount(4);
        winCombinations.put("4_times", winCombination2);
        when(config.getWin_combinations()).thenReturn(winCombinations);

        // Mock WinCombinations


        // Mock matrix
        List<List<String>> matrix = new ArrayList<>();

        // Act
        Output actualOutput = gameLogic.getOutput(matrix, config, 1);

        // Verify
        assertEquals(0, actualOutput.getReward(), 0.001);
    }

    private static Map<String, Symbol> getStringSymbolMap() {
        Map<String, Symbol> symbols = new HashMap<>();
        Symbol ASymbol = new Symbol();
        ASymbol.setType("standard");
        ASymbol.setReward_multiplier(1.0);
        symbols.put("A", ASymbol);
        Symbol BSymbol = new Symbol();
        BSymbol.setType("standard");
        BSymbol.setReward_multiplier(2.0);
        symbols.put("B", BSymbol);
        Symbol CSymbol = new Symbol();
        CSymbol.setType("standard");
        CSymbol.setReward_multiplier(3.0);
        symbols.put("C", CSymbol);
        return symbols;
    }
}
