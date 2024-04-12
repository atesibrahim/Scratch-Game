package org.cyber.config;

import com.google.gson.Gson;
import org.cyber.model.input.FileConfiguration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class JsonFileParser {
    private final Gson gson;

    public JsonFileParser(Gson gson) {
        this.gson = gson;
    }

    public Optional<FileConfiguration> parse(String filename) throws IOException {
        try (FileReader reader = new FileReader(filename)) {
            return Optional.ofNullable(gson.fromJson(reader, FileConfiguration.class));
        } catch (IOException e) {
            System.out.println("Error occurred during file read. Error: " + e.getMessage());
        }
        return Optional.empty();
    }
}
