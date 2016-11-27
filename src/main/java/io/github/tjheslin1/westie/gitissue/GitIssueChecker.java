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
package io.github.tjheslin1.westie.gitissue;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WestieChecker;
import io.github.tjheslin1.westie.infrastructure.GitIssues;
import io.github.tjheslin1.westie.infrastructure.JiraIssues;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Checks that all Git issues referenced in to-do comments are in the
 * open state.
 */
public class GitIssueChecker extends WestieChecker {

    private static final String GIT_TODO_REGEX_FORMAT = ".*//[ ]*TODO.*%s.*";

    private final GitIssues gitIssues;
    private final String gitRegex;

    public GitIssueChecker(GitIssues gitIssues, String gitRegex) {
        super(emptyList());
        this.gitIssues = gitIssues;
        this.gitRegex = gitRegex;
    }

    public GitIssueChecker(GitIssues gitIssues, String gitRegex, List<String> javaFilesToIgnore) {
        super(javaFilesToIgnore);
        this.gitIssues = gitIssues;
        this.gitRegex = gitRegex;
    }

    /**
     *
     * @param pathToCheck The package to check source files for to-do comments
     *                    which reference Git issues.
     * @return A list of violations in which to-do comments are referencing Git issues
     * which are not in the open state.
     * @throws IOException if an I/O error occurs when opening the directory.
     */
    public List<Violation> todosAreInOpenState(Path pathToCheck) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isAJavaFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::checkGitIssues)
                .peek(this::reportViolation)
                .collect(toList());
    }

    private Stream<Violation> checkGitIssues(Path file) {
        try {
            return Files.lines(file).collect(toList()).stream()
                    .filter(this::gitTodoLine)
                    .filter(this::gitIssueIsInNotInTheOpenState)
                    .map(gitTodoLine -> new Violation(file, gitTodoLine));
        } catch (IOException e) {
            return Stream.of(new Violation(file, "Unable to read file."));
        }
    }

    private boolean gitTodoLine(String line) {
        return line.matches(format(GIT_TODO_REGEX_FORMAT, gitRegex));
    }

    private boolean gitIssueIsInNotInTheOpenState(String line) {
        Pattern pattern = Pattern.compile(gitRegex);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String issue = matcher.group();
            return !gitIssues.isGitIssueOpen(issue);
        } else {
            throw new IllegalStateException(format("Unable to find Git Issue in line '%s' using regex '%s'", line, gitRegex));
        }
    }

    private void reportViolation(Violation violation) {
        System.out.println(format("Violation!%n'%s'%nThe above violation was caused by a reference to a " +
                "Git issue which is not in the open state.", violation));
    }
}
