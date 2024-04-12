### Scratch Game Application

This application runs on port 8080 by default. You can change the port by passing the `--server.port` argument to the application.


## Description ##

Problem statement: this application is scratch game which generates a matrix (for example 3x3) from symbols(based on probabilities for each individual cell) and based on winning combintations user either will win or lost.
User can place a bet with any amount which is called *betting amount* in this application.

There are two types of symbols: Standard Symbols, Bonus Symbols.

**Standard Symbols**: identifies if user won or lost the game based on winning combinations 
(combination can be X times repeated symbols or symbols that follow a specific pattern)
Bonus symbols are described the table below:


| Symbol Name | Reward Multiplier |
|-------------|-------------------|
| A           | 50                |
| B           | 25                |
| C           | 10                |
| D           | 5                 |
| E           | 3                 |
| F           | 1.5               |


**Bonus Symbols**: Bonus symbols are only effective when there are at least one winning combinations matches with the generated matrix.
Bonus symbols are described the table below:

| Symbol Name | Action                       |
|-------------|------------------------------|
| 10x         | mutiply final reward to 10   |
| 5x          | mutiply final reward to 5    |
| +1000       | add 1000 to the final reward |
| +500        | add 500 to the final reward  |
| MISS        | none                         |


The configuration file should be like below:

```json
{
    "columns": 3, // OPTIONAL
    "rows": 3, // OPTIONAL
    "symbols": {
        "A": {
            "reward_multiplier": 50,
            "type": "standard"               
        },
        "B": {
            "reward_multiplier": 25,
            "type": "standard"               
        },
        "C": {
            "reward_multiplier": 10,
            "type": "standard"               
        },
        "D": {
            "reward_multiplier": 5,
            "type": "standard"               
        },
        "E": {
            "reward_multiplier": 3,
            "type": "standard"               
        },
        "F": {
            "reward_multiplier": 1.5,
            "type": "standard"               
        },

        "10x": {
            "reward_multiplier": 10,
            "type": "bonus",
            "impact": "multiply_reward"
        },
        "5x": {
            "reward_multiplier": 5,
            "type": "bonus",
            "impact": "multiply_reward"
        },
        "+1000": {
            "extra": 1000,
            "type": "bonus",
            "impact": "extra_bonus"
        },
        "+500": {
            "extra": 500,
            "type": "bonus",
            "impact": "extra_bonus"
        },
        "MISS": {
            "type": "bonus",
            "impact": "miss"
        }
    },
    "probabilities": {
        "standard_symbols": [
            {
            "column": 0,
            "row": 0,
            "symbols": {
                "A": 1,
                "B": 2,
                "C": 3,
                "D": 4,
                "E": 5,
                "F": 6
            }
            },
            {
            "column": 0,
            "row": 1,
            "symbols": {
                "A": 1,
                "B": 2,
                "C": 3,
                "D": 4,
                "E": 5,
                "F": 6
            }
            }
            //...
        ],
        "bonus_symbols": {
            "symbols": {
                "10x": 1,
                "5x": 2,
                "+1000": 3,
                "+500": 4,
                "MISS": 5
            }
        }
    },
    "win_combinations": {
        "same_symbol_3_times": {
            "reward_multiplier": 1,
            "when": "same_symbols",
            "count": 3,
            "group": "same_symbols"
        },
        "same_symbol_4_times": {
            "reward_multiplier": 1.5,
            "when": "same_symbols",
            "count": 4,
            "group": "same_symbols"
        },
        "same_symbol_5_times": {
            "reward_multiplier": 2,
            "when": "same_symbols",
            "count": 5,
            "group": "same_symbols"
        },
        "same_symbol_6_times": {
            "reward_multiplier": 3,
            "when": "same_symbols",
            "count": 6,
            "group": "same_symbols"
        },
        "same_symbol_7_times": {
            "reward_multiplier": 5,
            "when": "same_symbols",
            "count": 7,
            "group": "same_symbols"
        },
        "same_symbol_8_times": {
            "reward_multiplier": 10,
            "when": "same_symbols",
            "count": 8,
            "group": "same_symbols"
        },
        "same_symbol_9_times": {
            "reward_multiplier": 20,
            "when": "same_symbols",
            "count": 9,
            "group": "same_symbols"
        },

        "same_symbols_horizontally": { // OPTIONAL
            "reward_multiplier": 2,
            "when": "linear_symbols",
            "group": "horizontally_linear_symbols",
            "covered_areas": [
                ["0:0", "0:1", "0:2"],
                ["1:0", "1:1", "1:1"],
                ["2:0", "2:1", "2:2"]
            ]
        },
        "same_symbols_vertically": { // OPTIONAL
            "reward_multiplier": 2,
            "when": "linear_symbols",
            "group": "vertically_linear_symbols",
            "covered_areas": [
                ["0:0", "1:0", "2:0"],
                ["0:1", "1:1", "2:1"],
                ["0:2", "1:2", "2:2"]
            ]
        },
        "same_symbols_diagonally_left_to_right": { // OPTIONAL
            "reward_multiplier": 5,
            "when": "linear_symbols",
            "group": "ltr_diagonally_linear_symbols",
            "covered_areas": [
                ["0:0", "1:1", "2:2"]
            ]
        },
        "same_symbols_diagonally_right_to_left": { // OPTIONAL
            "reward_multiplier": 5,
            "when": "linear_symbols",
            "group": "rtl_diagonally_linear_symbols",
            "covered_areas": [
                ["0:2", "1:1", "2:0"]
            ]
        }
    }
}
```

Description of each fields in the configuration file:

| field name                                           | description                                                                                                                                                                                                               |
|------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| columns                                              | number of columns in the matrix                                                                                                                                                                                           |
| rows                                                 | number of rows in the matrix                                                                                                                                                                                              |
| symbols                                              | list of symbols                                                                                                                                                                                                           |
| symbol.{X}.reward_multiplier                         | will multiply betting amount                                                                                                                                                                                              |
| symbol.{X}.type                                      | can be either standard or bonus                                                                                                                                                                                           |
| symbol.{X}.extra                                     | [only for bonuses] extra amount which will be added to the reward                                                                                                                                                         |
| symbol.{X}.impact                                    | [only for bonuses] fixed values: multiply_reward (which multiply final reward to *symbol.{X}.reward_multiplier*), extra_bonus(will add *symbol.{X}.extra* to the final reward), miss(nothing)                             |
| probabilities                                        | list of probabilities                                                                                                                                                                                                     |
| probabilities.standard_symbols                       | list of probabilities for standard symbols                                                                                                                                                                                |
| probabilities.standard_symbols[...].column           | column index                                                                                                                                                                                                              |
| probabilities.standard_symbols[...].row              | row index                                                                                                                                                                                                                 |
| probabilities.standard_symbols[...].symbols          | map of a symbol and it's probability number(to calculate to probability percentage just sum all symbols probability numbers and divide individual symbol's probability number to total probability numbers)               |
| probabilities.bonus_symbols                          | list of probabilities for bonus symbols                                                                                                                                                                                   |
| probabilities.bonus_symbols.symbols                  | map of a symbol and it's probability number(to calculate to probability percentage just sum all symbols probability numbers and divide individual symbol's probability number to total probability numbers)               |
| probabilities.win_combinations                       | list of winning combinations                                                                                                                                                                                              |
| probabilities.win_combinations.{X}.reward_multiplier | will multiply reward                                                                                                                                                                                                      |
| probabilities.win_combinations.{X}.count             | required count of the same symbols to activate the reward                                                                                                                                                                 |
| probabilities.win_combinations.{X}.group             | group which the winning combination belongs to, max 1 winning combination should be applied for each win combination group                                                                                                |
| probabilities.win_combinations.{X}.covered_areas     | array of array of strings which is described as "%d:%d" which demonstrates row and column number respectively                                                                                                             |
| probabilities.win_combinations.{X}.when              | fixed values: same_symbols (if one symbol repeated in the matrix *probabilities.win_combinations.{X}.count* times), linear_symbols(if it matches to *probabilities.win_combinations.{X}.covered_areas*)                   |

- Note: Fields which are marked as OPTIONAL, are not required but will add extra points to the candidate if the candidate implements it.
- Note (2): Bonus symbol can be generated randomly in any cell(s) in the matrix
- Note (3): If one symbols matches more than winning combinations then reward should be multiplied. formula: (SYMBOL_1 * WIN_COMBINATION_1_FOR_SYMBOL_1 * WIN_COMBINATION_2_FOR_SYMBOL_1)
- Note (4): If the more than one symbols matches any winning combinations then reward should be summed. formula: (SYMBOL_1 * WIN_COMBINATION_1_FOR_SYMBOL_1 * WIN_COMBINATION_2_FOR_SYMBOL_1) + (SYMBOL_2 * WIN_COMBINATION_1_FOR_SYMBOL_2)


### Architecture
The application uses Java 17 and Maven. 
Libraries are being used: gson, lombok, junit5, mockito

### Storage
The application uses in-memory list based.
This provides extremely fast way to store and retrieve data.

For calculating rewards collections(map) and stream are being used.

### Running the app
`java -jar scratch-game.jar --config <your-config-file> --betting-amount <your-betting-amount>`

For example after you hit the endpoint response will be look like the following for the URLs you provided:

```json
 {
  "matrix":[["+1000","D","F"],["F","E","D"],["B","B","B"]],
  "reward":6000.0,
  "applied_winning_combinations":{"B":["same_symbols_horizontally","same_symbol_3_times"]},
  "applied_bonus_symbol":"+1000"
}
```

| field name                   | description                                            |
|------------------------------|--------------------------------------------------------|
| matrix                       | generated 2D matrix                                    |
| reward                       | final reward which user won                            |
| applied_winning_combinations | Map of Symbol and List of applied winning combinations  |
| applied_bonus_symbol         | applied bonus symbol (can be null if the bonus is MISS |

Rewards breakdown:

| reward name             | reward details                    |
|-------------------------|-----------------------------------|
| symbol_A                | bet_amount x5                     |
| symbol_B                | bet_amount x3                     |
| same_symbol_5_times     | (reward for a specific symbol) x5 |
| same_symbol_3_times     | (reward for a specific symbol) x1 |
| same_symbols_vertically | (reward for a specific symbol) x2 |
| +1000                   | add 1000 extra to final reward    |

Calculations: (bet_amount x reward(symbol_A) x reward(same_symbol_5_times) x reward(same_symbols_vertically)) + (bet_amount x reward(symbol_B) x reward(same_symbol_3_times) x reward(same_symbols_vertically)) (+/x) reward(+1000) = (100 x5 x5 x2) + (100 x3 x1 x2) +1000 = 5000 + 600 + 1000 = 6600


### Running the tests
To run the tests you can use the following maven commands.
```mvn test```

Test Coverage

![Test Coverage](src/main/resources/test_coverage.png)