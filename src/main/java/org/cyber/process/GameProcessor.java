package org.cyber.process;

import com.google.gson.Gson;
import org.cyber.config.JsonFileParser;
import org.cyber.model.input.FileConfiguration;
import org.cyber.model.output.Output;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.cyber.constant.Constants.ERROR_READING_CONFIG_FILE;

public class GameProcessor {
    private final JsonFileParser jsonFileParser;
    private final Gson gson;
    private final MatrixGenerator matrixGenerator;
    private Output output;
    private final GameLogicImplementation gameLogicImplementation;

    public GameProcessor(JsonFileParser jsonFileParser, Gson gson,
                         MatrixGenerator matrixGenerator, Output output, GameLogicImplementation gameLogicImplementation) {
        this.jsonFileParser = jsonFileParser;
        this.gson = gson;
        this.matrixGenerator = matrixGenerator;
        this.output = output;
        this.gameLogicImplementation = gameLogicImplementation;
    }

    public Output processGameFile(String configFilename, int betAmount) {
        try {
            Optional<FileConfiguration> config = jsonFileParser.parse(configFilename);
            if (config.isEmpty()) return output;

            List<List<String>> matrix = matrixGenerator.generateMatrix(config.get());

            return gameLogicImplementation.getOutput(matrix, config.get(), betAmount);
        } catch (IOException e) {
            System.out.println(ERROR_READING_CONFIG_FILE + e.getMessage());
        }
        return output;
    }
}
