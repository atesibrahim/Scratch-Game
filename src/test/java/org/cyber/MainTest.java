package org.cyber;

import com.google.gson.Gson;
import org.cyber.config.JsonFileParser;
import org.cyber.model.input.FileConfiguration;
import org.cyber.model.output.Output;
import org.cyber.process.GameLogicImplementation;
import org.cyber.process.MatrixGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MainTest {

    private Gson gsonMock;
    private JsonFileParser jsonFileParserMock;
    private MatrixGenerator matrixGeneratorMock;
    private Output outputMock;
    private GameLogicImplementation gameLogicImplementationMock;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        gsonMock = mock(Gson.class);
        jsonFileParserMock = mock(JsonFileParser.class);
        matrixGeneratorMock = mock(MatrixGenerator.class);
        outputMock = mock(Output.class);
        gameLogicImplementationMock = mock(GameLogicImplementation.class);
    }

    @Test
    void testMainMethod() throws IOException {
        // Arrange
        String[] args = {"--config", "config.json", "--betting-amount", "100"};
        FileConfiguration fileConfiguration = new FileConfiguration();
        when(jsonFileParserMock.parse("config.json")).thenReturn(Optional.of(fileConfiguration));
        // Mock matrix
        List<List<String>> matrix = new ArrayList<>();
        matrix.add(Arrays.asList("A", "D", "E"));
        matrix.add(Arrays.asList("A", "B", "C"));
        matrix.add(Arrays.asList("A", "F", "C"));
        when(matrixGeneratorMock.generateMatrix(any())).thenReturn(matrix);
        //Mock output
        Output mockOutput = mock(Output.class);
        when(gameLogicImplementationMock.getOutput(any(), any(), anyInt())).thenReturn(mockOutput);
        //Mock jsonOutput
        String jsonOutput = "{\"reward\":0.0}";
        System.setOut(new PrintStream(outContent));
        when(gsonMock.toJson(any())).thenReturn(jsonOutput);

        // Act
        Main.main(args);

        // Assert
        assertEquals(jsonOutput, outContent.toString().substring(outContent.size()-16, outContent.size()-2));
    }

    @Test
    void testMainMethod_whenArgs_notProvided() {
        // Arrange
        String[] args = {};
        //Mock jsonOutput
        String jsonOutput = "It seems your arguments are not correct. Please check your arguments!" +
                " Usage: java -jar scratch-game.jar --config <your-config-json-file> --betting-amount <betting-amount>";
        System.setOut(new PrintStream(outContent));

        // Act
        Main.main(args);

        // Assert
        assertEquals(jsonOutput, outContent.toString().substring(0, outContent.size()-2));
    }

    @Test
    void testMainMethod_whenFile_notProvided() throws IOException {
        // Arrange
        String[] args = {"--config", "", "--betting-amount"};
        //Mock jsonOutput
        String jsonOutput = "Config file not provided.";
        System.setOut(new PrintStream(outContent));

        // Act
        Main.main(args);

        // Assert
        assertEquals(jsonOutput, outContent.toString().substring(0, outContent.size()-2));
    }

    @Test
    void testMainMethod_whenBettingAmountFormat_wrong() throws IOException {
        // Arrange
        String[] args = {"--config", "config.json", "--betting-amount", "as"};
        //Mock jsonOutput
        String jsonOutput = "Invalid betting amount. Please provide a valid integer.";
        System.setOut(new PrintStream(outContent));

        // Act
        Main.main(args);

        // Assert
        assertEquals(jsonOutput, outContent.toString().substring(0, outContent.size()-2));
    }

    @Test
    void testMainMethod_whenBettingAmount_notProvided() throws IOException {
        // Arrange
        String[] args = {"--config", "config.json", "--betting-amount"};
        //Mock jsonOutput
        String jsonOutput = "Betting amount must be greater than 0.";
        System.setOut(new PrintStream(outContent));

        // Act
        Main.main(args);

        // Assert
        assertEquals(jsonOutput, outContent.toString().substring(0, outContent.size()-2));
    }
}