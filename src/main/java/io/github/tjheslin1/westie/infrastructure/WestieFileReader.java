package io.github.tjheslin1.westie.infrastructure;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * TODO
 */
@FunctionalInterface
public interface WestieFileReader {

    /**
     * TODO
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    List<String> readAllLines(Path filePath) throws IOException;
}
