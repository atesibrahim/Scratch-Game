package org.cyber.model.input;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Probabilities implements Serializable {
    private List<StandardSymbolProbability> standard_symbols;
    private BonusSymbols bonus_symbols;
}
