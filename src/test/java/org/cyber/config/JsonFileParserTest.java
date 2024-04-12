package org.cyber.config;

import com.google.gson.Gson;
import org.cyber.model.input.FileConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonFileParserTest {
    private Gson gsonMock;
    private JsonFileParser jsonFileParser;

    @BeforeEach
    void setUp() {
        gsonMock = mock(Gson.class);
        jsonFileParser = new JsonFileParser(gsonMock);
    }

    @Test
    void testParse_ValidFile_ReturnsFileConfiguration() throws IOException {
        // Arrange
        String filename = "src/test/resources/config.json";
        FileConfiguration expectedConfig = new FileConfiguration();
        when(gsonMock.fromJson(any(FileReader.class), eq(FileConfiguration.class))).thenReturn(expectedConfig);

        // Act
        Optional<FileConfiguration> result = jsonFileParser.parse(filename);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedConfig, result.get());
        verify(gsonMock).fromJson(any(FileReader.class), eq(FileConfiguration.class));
    }

    @Test
    void testParse_InvalidFile_ReturnsEmptyOptional() throws IOException {
        // Arrange
        String filename = "invalid_config.json";

        // Act
        Optional<FileConfiguration> result = jsonFileParser.parse(filename);

        // Assert
        assertFalse(result.isPresent());
        assertEquals(Optional.empty(), result);
    }

    @Test
    void testParse_IOError_ReturnsEmptyOptional() throws IOException {
        // Arrange
        String filename = "config.json";

        // Act
        Optional<FileConfiguration> result = jsonFileParser.parse(filename);

        // Assert
        assertFalse(result.isPresent());
        assertEquals(Optional.empty(), result);
    }
}