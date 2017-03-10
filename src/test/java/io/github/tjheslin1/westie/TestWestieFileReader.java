package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TestWestieFileReader implements WestieFileReader {
    @Override
    public List<String> readAllLines(Path filePath) throws IOException {
        return Files.readAllLines(filePath);
    }
}
