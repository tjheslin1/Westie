/*
 * Copyright 2016 Thomas Heslin <tjheslin1@gmail.com>.
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

import io.github.tjheslin1.westie.FileLineViolation;
import io.github.tjheslin1.westie.WestieChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static io.github.tjheslin1.westie.WestieRegexes.TODO_REGEX;
import static java.util.stream.Collectors.toList;

/**
 * Enforces that to-do comments follow a structure.
 * <p>
 * For example: to-dos must contain a date in the format specified by a regex.
 */
public class TodosStructureChecker extends WestieChecker {

    private final String todosStructureRegex;

    public TodosStructureChecker(String todosStructureRegex, List<String> javaFilesToIgnore) {
        super(javaFilesToIgnore);
        this.todosStructureRegex = todosStructureRegex;
    }

    /**
     * @param pathToCheck The package to check source files for to-do comments' structure.
     * @return A list of violations in which to-do comments do not follow the specified pattern.
     * @throws IOException if an I/O error occurs when opening the directory.
     */
    public List<FileLineViolation> checkAllTodosFollowExpectedStructure(Path pathToCheck) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isAJavaFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::verifyStructureOfTodos)
                .peek(FileLineViolation::reportViolation)
                .collect(toList());
    }

    private Stream<FileLineViolation> verifyStructureOfTodos(Path file) {
        try {
            return Files.lines(file)
                    .filter(this::lineContainsTodo)
                    .filter(this::linesNotConformingToStructure)
                    .map(todoLine -> new FileLineViolation(file, todoLine,
                            "Violation was caused by the TODO not matching structure with regex: " + todosStructureRegex));
        } catch (IOException e) {
            return Stream.of(new FileLineViolation(file, "Unable to read file.", e.getMessage()));
        }
    }

    private boolean lineContainsTodo(String line) {
        return line.matches(TODO_REGEX);
    }

    private boolean linesNotConformingToStructure(String todoLine) {
        return !todoLine.matches(todosStructureRegex);
    }
}
