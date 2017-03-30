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
 * Base class of a static analysis test with useful methods to reuse.
 */
public class WestieAnalyser {

    public static final WestieCachedFileReader CACHED_FILE_READER = new WestieCachedFileReader();

    private final WestieFileReader fileReader;

    public WestieAnalyser() {
        this.fileReader = CACHED_FILE_READER;
    }

    public WestieAnalyser(WestieFileReader fileReader) {
        this.fileReader = fileReader;
    }

    public WestieAnalyserForDirectory analyseDirectory(Path pathToCheck) {
        if(!Files.isDirectory(pathToCheck)) {
            throw new IllegalArgumentException(format("Expected a directory. '%s' was provided.", pathToCheck));
        }
        return new WestieAnalyserForDirectory(fileReader, pathToCheck);
    }
}
