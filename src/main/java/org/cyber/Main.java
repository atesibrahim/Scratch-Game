package org.cyber;

import com.google.gson.Gson;
import org.cyber.config.JsonFileParser;
import org.cyber.model.output.Output;
import org.cyber.process.GameLogicImplementation;
import org.cyber.process.GameProcessor;
import org.cyber.process.MatrixGenerator;

public class Main {

    private static final boolean IS_ADDED_BONUS_SYMBOL_BEFORE = false;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("It seems your arguments are not correct. Please check your arguments!" +
                    " Usage: java -jar scratch-game.jar --config <your-config-json-file> --betting-amount <betting-amount>");
            return;
        }

        String configFilename = null;
        int betAmount = 0;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--config") && i + 1 < args.length) {
                configFilename = args[i + 1];
            } else if (args[i].equals("--betting-amount") && i + 1 < args.length) {
                try {
                    betAmount = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid betting amount. Please provide a valid integer.");
                    return;
                }
            }
        }

        if (configFilename == null || configFilename.isEmpty()) {
            System.out.println("Config file not provided.");
            return;
        }


        if (betAmount <= 0) {
            System.out.println("Betting amount must be greater than 0.");
            return;
        }
        Gson gson = new Gson();
        JsonFileParser jsonFileParser = new JsonFileParser(gson);
        MatrixGenerator matrixGenerator = new MatrixGenerator(IS_ADDED_BONUS_SYMBOL_BEFORE);
        Output output = Output.builder().build();
        GameLogicImplementation gameLogicImplementation = new GameLogicImplementation(output);
        GameProcessor gameProcessor = new GameProcessor(jsonFileParser, gson, matrixGenerator, output, gameLogicImplementation);
        output = gameProcessor.processGameFile(configFilename, betAmount);

        System.out.println(gson.toJson(output));
    }
}