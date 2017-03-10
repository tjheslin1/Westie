package io.github.tjheslin1.westie.infrastructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.String.format;

/**
 * TODO
 */
public class WestieCachedFileReader implements WestieFileReader {

    /**
     * TODO
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    @Override
    public List<String> readAllLines(Path filePath) throws IOException {
        if (!Files.isRegularFile(filePath)) {
            throw new IllegalStateException(format("Expected a file to read. Instead provided: '%s'", filePath));
        }
        return Files.readAllLines(filePath);
    }
}