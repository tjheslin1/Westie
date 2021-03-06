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
package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.infrastructure.WestieCachedFileReader;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;

/**
 * Starting point of the analysis API.
 */
public class WestieAnalyser {

    private static final WestieCachedFileReader CACHED_FILE_READER = new WestieCachedFileReader();

    private final WestieFileReader fileReader;

    public WestieAnalyser() {
        this.fileReader = CACHED_FILE_READER;
    }

    public WestieAnalyser(WestieFileReader fileReader) {
        this.fileReader = fileReader;
    }

    /**
     * Analyse all regular files, recursively, under the given directory.
     *
     * @param pathToCheck The directory under which to recursively search and apply analysis on files.
     * @return A {@link WestieDirectoryAnalyserBuilder} using the provided or default {@link WestieFileReader}.
     */
    public WestieDirectoryAnalyserBuilder analyseDirectory(Path pathToCheck) {
        if (!Files.isDirectory(pathToCheck)) {
            throw new IllegalArgumentException(format("Expected a directory. '%s' was provided.", pathToCheck));
        }
        return new WestieDirectoryAnalyserBuilder(fileReader, pathToCheck);
    }

    public WestieFileAnalyser analyseFile(Path fileToCheck) {
        if (!Files.isRegularFile(fileToCheck)) {
            throw new IllegalArgumentException(format("Expected a regular file. '%s' was provided.", fileToCheck));
        }

        return new WestieFileAnalyser(fileToCheck, fileReader);
    }
}
