package io.github.tjheslin1.westie.infrastructure;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.String.format;

/**
 * TODO
 */
public class WestieCachedFileReader implements WestieFileReader {

    private final FileLinesReader fileLinesReader;

    public WestieCachedFileReader() {
        this.fileLinesReader = new FileLinesReader();
    }

    public WestieCachedFileReader(FileLinesReader fileLinesReader) {
        this.fileLinesReader = fileLinesReader;
    }

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
            throw new IllegalStateException(format("Expected a file to read. Instead was provided: '%s'", filePath));
        }
        return filesLinesCache.getUnchecked(filePath);
    }


    private final LoadingCache<Path, List<String>> filesLinesCache = CacheBuilder.<Path, List<String>>newBuilder()
            .build(new CacheLoader<Path, List<String>>() {
                @Override
                public List<String> load(Path key) throws IOException {
                    return fileLinesReader.readAllLines(key);
                }
            });
}