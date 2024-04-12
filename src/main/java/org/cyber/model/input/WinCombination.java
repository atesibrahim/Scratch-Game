package org.cyber.model.input;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WinCombination {
    private double reward_multiplier;
    private int count;
    private String group;
    private List<List<String>> covered_areas;
}
