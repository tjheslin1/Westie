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
package io.github.tjheslin1.westie.todostructure;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WestieAnalyser;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static io.github.tjheslin1.westie.WestieRegexes.TODO_REGEX;

/**
 * Enforces that to-do comments follow a structure.
 * <p>
 * For example: to-dos must contain a date in the format specified by a regex.
 */
public class TodosStructureAnalyser extends WestieAnalyser {

    private final String todosStructureRegex;

    public TodosStructureAnalyser(String todosStructureRegex) {
        super();
        this.todosStructureRegex = todosStructureRegex;
    }

    public TodosStructureAnalyser(String todosStructureRegex, WestieFileReader fileReader, List<String> javaFilesToIgnore) {
        super(fileReader, javaFilesToIgnore);
        this.todosStructureRegex = todosStructureRegex;
    }

    /**
     * @param pathToCheck The package to check source files for to-do comments' structure.
     * @return A list of violations in which to-do comments do not follow the specified pattern.
     * @throws IOException if an I/O error occurs when opening the directory.
     */
    public List<Violation> checkAllTodosFollowExpectedStructure(Path pathToCheck) throws IOException {
        return new WestieAnalyser()
                .analyseDirectory(pathToCheck)
                .forJavaFiles()
                //.lineByLine() // TODO
                .analyse(this::todosFollowStructure,
                        "Violation was caused by the TODO not matching structure with regex: " + todosStructureRegex);
    }

    private boolean todosFollowStructure(String fileLine) {
        return lineContainsTodo(fileLine) && !lineConformsToStructure(fileLine);
    }

    private boolean lineContainsTodo(String line) {
        return line.matches(TODO_REGEX);
    }

    private boolean lineConformsToStructure(String todoLine) {
        return todoLine.matches(todosStructureRegex);
    }
}
