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
package io.github.tjheslin1.westie.jiraissue;

import io.github.tjheslin1.westie.FileLineViolation;
import io.github.tjheslin1.westie.WestieChecker;
import io.github.tjheslin1.westie.infrastructure.JiraIssues;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * Checks that the status of all Jira issues, referenced in to-do comments,
 * are in an accepted state.
 */
public class JiraReferenceChecker extends WestieChecker {

    private static final String JIRA_TODO_REGEX_FORMAT = ".*//.*(T|t)(O|o)(D|d)(O|o).*%s.*";

    private final JiraIssues jiraIssues;
    private final String jiraRegex;

    public JiraReferenceChecker(JiraIssues jiraIssues, String jiraRegex) {
        super();
        this.jiraIssues = jiraIssues;
        this.jiraRegex = jiraRegex;
    }

    public JiraReferenceChecker(JiraIssues jiraIssues, String jiraRegex, WestieFileReader fileReader, List<String> javaFilesToIgnore) {
        super(fileReader, javaFilesToIgnore);
        this.jiraIssues = jiraIssues;
        this.jiraRegex = jiraRegex;
    }

    /**
     * @param pathToCheck The package to check source files for to-do comments
     *                    which reference Jira issues.
     * @return A list of violations in which to-do comments are referencing Jira issues
     * which are not in the list of accepted states, defined in {@link JiraIssues}.
     * @throws IOException if an I/O error occurs when opening the directory.
     */
    public List<FileLineViolation> todosAreInAllowedStatuses(Path pathToCheck) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isAJavaFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::checkJiraTodos)
                .peek(FileLineViolation::reportViolation)
                .collect(toList());
    }

    private Stream<FileLineViolation> checkJiraTodos(Path file) {
        try {
            return allFileLines(file).stream()
                    .filter(this::jiraTodoLine)
                    .filter(this::jiraIssueInUnacceptedState)
                    .map(jiraTodoLine -> new FileLineViolation(file, jiraTodoLine, format("Violation was caused by a reference to a " +
                            "Jira issue which is not in any of the accepted statuses: '%s'.", jiraIssues.allowedStatuses())));
        } catch (IOException e) {
            return Stream.of(new FileLineViolation(file, "Unable to read file.", e.getMessage()));
        }
    }

    private boolean jiraTodoLine(String line) {
        return line.matches(format(JIRA_TODO_REGEX_FORMAT, jiraRegex));
    }

    private boolean jiraIssueInUnacceptedState(String line) {
        Pattern pattern = Pattern.compile(jiraRegex);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String jiraIssue = matcher.group();
            return !jiraIssues.isJiraIssueInAllowedStatus(jiraIssue);
        } else {
            throw new IllegalStateException(format("Unable to find Jira Issue in line '%s' using regex '%s'", line, jiraRegex));
        }
    }
}
