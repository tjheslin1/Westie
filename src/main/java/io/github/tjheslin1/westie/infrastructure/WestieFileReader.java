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

import io.github.tjheslin1.westie.WestieAnalyser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * File reading used by {@link WestieAnalyser} extensions to
 * perform static analysis on project files.
 */
@FunctionalInterface
public interface WestieFileReader {

    /**
     * Reads the lines of a file and caches the result.
     *
     * @param filePath The {@link Path} to the file to read.
     * @return The lines of the file as a list of Strings.
     * @throws IOException If an exception occurs reading the file.
     */
    List<String> readAllLines(Path filePath) throws IOException;
}
