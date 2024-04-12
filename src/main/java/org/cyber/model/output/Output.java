package org.cyber.model.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class Output {
    private List<List<String>> matrix;
    private double reward;
    private Map<String, List<String>> applied_winning_combinations;
    private String applied_bonus_symbol;
}
