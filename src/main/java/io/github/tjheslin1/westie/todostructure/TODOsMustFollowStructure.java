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

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.StaticAnalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class TodosMustFollowStructure extends StaticAnalysis {

    private static final String TODO_REGEX = ".*//[ ]*TODO.*";

    private final String todosStructureRegex;

    public TodosMustFollowStructure(String todosStructureRegex, List<String> javaFilesToIgnore) {
        super(javaFilesToIgnore);
        this.todosStructureRegex = todosStructureRegex;
    }

    public List<Violation> checkAllTodosFollowExpectedStructure(Path pathToCheck) throws Exception {
        return Files.list(pathToCheck)
                .filter(this::isAJavaFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::verifyStructureOfTodos)
                .peek(this::reportViolation)
                .collect(toList());
    }

    private Stream<Violation> verifyStructureOfTodos(Path file) {
        try {
            return Files.lines(file)
                    .filter(this::lineContainsTodo)
                    .filter(this::linesNotConformingToStructure)
                    .map(todoLine -> new Violation(file, todoLine));
        } catch (IOException e) {
            return Stream.of(new Violation(file, "Unable to read file."));
        }
    }

    private boolean lineContainsTodo(String line) {
        return line.matches(TODO_REGEX);
    }

    private boolean linesNotConformingToStructure(String todoLine) {
        return !todoLine.matches(todosStructureRegex);
    }

    private void reportViolation(Violation violation) {
        System.out.println(format("Violation!%n'%s'%nThe above violation was caused by the TODO not matching structure with regex: '%s'. " +
                        "%nSpecified in the Westie class: %s%n",
                violation, todosStructureRegex, this.getClass().getSimpleName()));
    }
}
