package org.cyber.process;

import org.cyber.model.input.FileConfiguration;
import org.cyber.model.input.WinCombination;
import org.cyber.model.output.Output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.cyber.constant.Constants.IS_LEFT_TO_RIGHT_FALSE;
import static org.cyber.constant.Constants.IS_LEFT_TO_RIGHT_TRUE;
import static org.cyber.constant.Constants.MULTIPLE_SIGN;
import static org.cyber.constant.Constants.PLUS_SIGN;
import static org.cyber.constant.Constants.SAME_SYMBOLS_DIAGONALLY_LEFT_TO_RIGHT;
import static org.cyber.constant.Constants.SAME_SYMBOLS_DIAGONALLY_RIGHT_TO_LEFT;
import static org.cyber.constant.Constants.SAME_SYMBOLS_HORIZONTALLY;
import static org.cyber.constant.Constants.SAME_SYMBOLS_VERTICALLY;
import static org.cyber.constant.Constants.SYMBOLS_STRING;
import static org.cyber.constant.Constants.TIMES_STRING;

public class GameLogicImplementation {
    private final Map<Integer, String> sameSymbolTimesMap;
    private final Map<String, Double> sameSymbolShapesMap;
    private final Map<String, Integer> bonusRewardMultiplierMap;
    private final Map<String, Integer> bonusRewardSumMap;
    private final Map<String, List<String>> appliedWinningCombinations;
    private double toApplyBonusMultiplier;
    private double toApplyBonusSum;
    private final Output output;

    public GameLogicImplementation(Output output) {
        this.output = output;
        this.sameSymbolTimesMap = new HashMap<>();
        this.sameSymbolShapesMap = new HashMap<>();
        this.bonusRewardMultiplierMap = new HashMap<>();
        this.bonusRewardSumMap = new HashMap<>();
        this.appliedWinningCombinations = new HashMap<>();
        this.toApplyBonusSum = 0;
        this.toApplyBonusMultiplier = 1;
    }

    public Output getOutput(List<List<String>> matrix, FileConfiguration fileConfiguration, int betAmount) {
        output.setMatrix(matrix);
        output.setReward(calculateReward(matrix, fileConfiguration, betAmount));
        output.setApplied_winning_combinations(appliedWinningCombinations);
        return output;
    }

    private double calculateReward(List<List<String>> matrix, FileConfiguration fileConfiguration, int betAmount) {
        double totalReward = 0;
        if (isFileConfigurationOrWinCombinationNull(fileConfiguration)) {
            return totalReward;
        }
        if (matrix.isEmpty() || matrix.get(0).isEmpty()) {
            return totalReward;
        }

        mapSymbols(fileConfiguration);
        Map<String, Integer> symbolCounts = countSymbols(matrix);

        for (Map.Entry<String, Integer> entry : symbolCounts.entrySet()) {
            String symbol = entry.getKey();
            int count = entry.getValue();

            if (sameSymbolTimesMap.containsKey(count)) {
                calculateRewardIfHasBonusRewards(matrix);
                checkShapeIfSame(matrix, symbol);
                WinCombination winCombination = fileConfiguration.getWin_combinations().get(sameSymbolTimesMap.get(count));

                totalReward += betAmount * winCombination.getReward_multiplier() * fileConfiguration.getSymbols().get(symbol).getReward_multiplier();

                appliedWinningCombinations.putIfAbsent(symbol, new ArrayList<>());
                appliedWinningCombinations.get(symbol).add(sameSymbolTimesMap.get(count));
            }
        }
        return totalReward * toApplyBonusMultiplier + toApplyBonusSum;
    }

    private boolean isFileConfigurationOrWinCombinationNull(FileConfiguration fileConfiguration) {
        if (fileConfiguration == null) {
            System.out.println("File configuration is null");
            return true;
        }
        if (fileConfiguration.getWin_combinations().isEmpty()) {
            System.out.println("File configuration win combination is null");
            return true;
        }
        return false;
    }

    private void mapSymbols(FileConfiguration fileConfiguration) {
        mapSameSymbols(fileConfiguration);
        mapBonusSymbols(fileConfiguration);
    }

    private void mapSameSymbols(FileConfiguration fileConfiguration) {
        for (String key : fileConfiguration.getWin_combinations().keySet()) {
            if (key.contains(TIMES_STRING)) {
                sameSymbolTimesMap.put(fileConfiguration.getWin_combinations().get(key).getCount(), key);
            } else if (key.contains(SYMBOLS_STRING)) {
                sameSymbolShapesMap.put(key, fileConfiguration.getWin_combinations().get(key).getReward_multiplier());
            }
        }
    }

    private void mapBonusSymbols(FileConfiguration fileConfiguration) {
        if (fileConfiguration.getProbabilities() == null ||
                fileConfiguration.getProbabilities().getBonus_symbols() == null ||
                fileConfiguration.getProbabilities().getBonus_symbols().getSymbols() == null) return;
        for (String key : fileConfiguration.getProbabilities().getBonus_symbols().getSymbols().keySet()) {
            if (key.contains(MULTIPLE_SIGN)) {
                bonusRewardMultiplierMap.put(key, Integer.valueOf(key.substring(0, key.length() - 1)));
            } else if (key.contains(PLUS_SIGN)) {
                bonusRewardSumMap.put(key, Integer.valueOf(key.substring(1)));
            }
        }
    }

    private Map<String, Integer> countSymbols(List<List<String>> matrix) {
        return matrix.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        symbol -> symbol,
                        symbol -> 1,
                        Integer::sum
                ));
    }

    private void calculateRewardIfHasBonusRewards(List<List<String>> matrix) {
        matrix.forEach(row ->
                row.forEach(symbol -> {
                    if (bonusRewardMultiplierMap.containsKey(symbol)) {
                        output.setApplied_bonus_symbol(symbol);
                        toApplyBonusMultiplier *= bonusRewardMultiplierMap.getOrDefault(symbol, 1);
                    } else if (bonusRewardSumMap.containsKey(symbol)) {
                        output.setApplied_bonus_symbol(symbol);
                        toApplyBonusSum += bonusRewardSumMap.getOrDefault(symbol, 0);
                    }
                })
        );
    }

    private void checkShapeIfSame(List<List<String>> matrix, String symbol) {
        checkVerticallySame(matrix, symbol);
        checkHorizontallySame(matrix, symbol);
        checkDiagonallySame(matrix, symbol, IS_LEFT_TO_RIGHT_TRUE);
        checkDiagonallySame(matrix, symbol, IS_LEFT_TO_RIGHT_FALSE);
    }

    private void checkVerticallySame(List<List<String>> matrix, String symbol) {
        int cols = matrix.get(0).size();
        boolean verticallySame = false;
        for (int col = 0; col < cols; col++) {
            final int finalCol = col;
            verticallySame = matrix.stream().allMatch(row -> row.get(finalCol).equals(matrix.get(0).get(finalCol)));
            if (verticallySame) break;
        }

        if (verticallySame) {
            appliedWinningCombinations.computeIfAbsent(symbol, k -> new ArrayList<>()).add(SAME_SYMBOLS_VERTICALLY);
            toApplyBonusMultiplier *= sameSymbolShapesMap.getOrDefault(SAME_SYMBOLS_VERTICALLY, 1.0);
        }
    }

    private void checkHorizontallySame(List<List<String>> matrix, String symbol) {
        boolean horizontallySame = matrix.stream().anyMatch(row -> row.stream().distinct().count() == 1);
        if (horizontallySame) {
            appliedWinningCombinations.computeIfAbsent(symbol, k -> new ArrayList<>()).add(SAME_SYMBOLS_HORIZONTALLY);
            toApplyBonusMultiplier *= sameSymbolShapesMap.getOrDefault(SAME_SYMBOLS_HORIZONTALLY, 1.0);
        }
    }

    private void checkDiagonallySame(List<List<String>> matrix, String symbol, boolean leftToRight) {
        int numRows = matrix.size();
        int numCols = matrix.get(0).size();
        boolean isDiagonalEqual = true;

        for (int row = 0; row < numRows - 1; row++) {
            int col = leftToRight ? row : numCols - row - 1;
            int nextRow = row + 1;
            int nextCol = leftToRight ? col + 1 : col - 1;

            if (!matrix.get(row).get(col).equals(matrix.get(nextRow).get(nextCol))) {
                isDiagonalEqual = false;
                break;
            }
        }

        if (isDiagonalEqual) {
            String diagonalType = leftToRight ? SAME_SYMBOLS_DIAGONALLY_LEFT_TO_RIGHT : SAME_SYMBOLS_DIAGONALLY_RIGHT_TO_LEFT;
            appliedWinningCombinations.computeIfAbsent(symbol, k -> new ArrayList<>()).add(diagonalType);
            toApplyBonusMultiplier *= sameSymbolShapesMap.getOrDefault(diagonalType, 1.0);
        }
    }
}
