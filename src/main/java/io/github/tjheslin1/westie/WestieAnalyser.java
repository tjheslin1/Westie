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
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

/**
 * Base class of a static analysis test with useful methods to reuse.
 */
public class WestieAnalyser {

    public static final WestieCachedFileReader CACHED_FILE_READER = new WestieCachedFileReader();

    private final WestieFileReader fileReader;
    private final List<String> filesToIgnore;

    private Path pathToCheck;

    public WestieAnalyser() {
        this.fileReader = CACHED_FILE_READER;
        this.filesToIgnore = emptyList();
    }

    public WestieAnalyser(List<String> filesToIgnore) {
        this.fileReader = CACHED_FILE_READER;
        this.filesToIgnore = filesToIgnore;
    }

    public WestieAnalyser(List<String> filesToIgnore, WestieFileReader fileReader) {
        this.fileReader = fileReader;
        this.filesToIgnore = filesToIgnore;
    }

    public WestieAnalyserForDirectory analyseDirectory(Path pathToCheck) {
        if(!Files.isDirectory(pathToCheck)) {
            throw new IllegalArgumentException(format("Expected a directory. '%s' was provided.", pathToCheck));
        }
        this.pathToCheck = pathToCheck;
        return new WestieAnalyserForDirectory(fileReader, filesToIgnore, pathToCheck);
    }

    /**
     * @param file Path to a source file.
     * @return 'true' if the file's extension is '.java'.
     */
    protected boolean isAJavaFile(Path file) {
        return file.toString().endsWith(".java");
    }

    /**
     * @param file Path to a source file.
     * @return 'true' if the file's extension is '.properties'.
     */
    protected boolean isAPropertiesFile(Path file) {
        return file.toString().endsWith(".properties");
    }

    /**
     * @param file Path to a source file.
     * @return 'true' if the file provided does not appear in the provided list of 'filesToIgnore'.
     */

    /**
     * Extracts the filename of a file from its full path.
     *
     * @param file Path to a source file.
     * @return The filename and extension of a file, provided its path.
     */
    protected String filenameFromPath(Path file) {
        return file.subpath(file.getNameCount() - 1, file.getNameCount()).toString();
    }
}
