package org.cyber.process;

import com.google.gson.Gson;
import org.cyber.config.JsonFileParser;
import org.cyber.model.input.FileConfiguration;
import org.cyber.model.output.Output;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameProcessorTest {

    @Test
    public void testFileProcessing_SuccessfulParsing_OutputJson() throws IOException {
        // Arrange
        String configFilename = "validConfig.json";
        int betAmount = 10;
        Gson gson = new Gson();
        JsonFileParser parserMock = mock(JsonFileParser.class);
        FileConfiguration validConfig = new FileConfiguration(); // Create a valid config
        when(parserMock.parse(configFilename)).thenReturn(Optional.of(validConfig));

        MatrixGenerator matrixGeneratorMock = mock(MatrixGenerator.class);
        List<List<String>> mockMatrix = new ArrayList<>(); // Create a mock matrix
        when(matrixGeneratorMock.generateMatrix(validConfig)).thenReturn(mockMatrix);

        Output mockOutput = Output.builder().applied_bonus_symbol("applied").reward(15).build(); // Create a mock output
        GameLogicImplementation gameLogicImplementationMock = mock(GameLogicImplementation.class);
        when(gameLogicImplementationMock.getOutput(mockMatrix, validConfig, betAmount)).thenReturn(mockOutput);

        GameProcessor gameProcessor = new GameProcessor(parserMock, gson, matrixGeneratorMock, mockOutput, gameLogicImplementationMock);

        // Act
        Output actual = gameProcessor.processGameFile(configFilename, betAmount);

        // Assert
        assertEquals(mockOutput.getApplied_bonus_symbol(), actual.getApplied_bonus_symbol());
        assertEquals(mockOutput.getReward(), actual.getReward());

        //Verify
        verify(parserMock, times(1)).parse(configFilename);
        verify(matrixGeneratorMock, times(1)).generateMatrix(validConfig);
        verify(gameLogicImplementationMock, times(1)).getOutput(mockMatrix, validConfig, betAmount);
    }

    @Test
    public void testFileProcessing_ConfigFileNotFound_ReturnsNull() throws IOException {
        // Arrange
        String configFilename = "nonexistentConfig.json";
        int betAmount = 10;
        JsonFileParser parserMock = mock(JsonFileParser.class);
        when(parserMock.parse(configFilename)).thenReturn(Optional.empty());

        GameProcessor gameProcessor = new GameProcessor(parserMock, null, null, null, null);


        // Act
        Output actual = gameProcessor.processGameFile(configFilename, betAmount);

        // Assert
        assertNull(actual);

        //Verify
        verify(parserMock, times(1)).parse(configFilename);
    }

    @Test
    public void testFileProcessing_ConfigFileNotFound_ReturnsException() throws IOException {
        // Arrange
        String configFilename = "nonexistentConfig.json";
        int betAmount = 10;
        JsonFileParser parserMock = mock(JsonFileParser.class);
        when(parserMock.parse(configFilename)).thenThrow(FileNotFoundException.class);

        GameProcessor gameProcessor = new GameProcessor(parserMock, null, null, null, null);

        // Act
        Output actual = gameProcessor.processGameFile(configFilename, betAmount);

        // Assert
        assertNull(actual);

        //Verify
        verify(parserMock, times(1)).parse(configFilename);
    }
}