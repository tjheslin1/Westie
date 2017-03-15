/*
 * Copyright 2017 Thomas Heslin <tjheslin1@gmail.com>.
 *
 * This file is part of Westie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * Reads the lines of a file and caches the result.
 * Subsequent reads to the same file, based on the
 * {@link Path} provided, lookup in the cache instead.
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
     * Reads the lines of a file and caches the result.
     *
     * @param filePath The {@link Path} to the file to read.
     * @return The lines of the file as a list of Strings.
     * @throws IOException If an exception occurs reading the file.
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