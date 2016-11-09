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
package io.github.tjheslin1.westie.jiratodoformat;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.StaticAnalysis;
import io.github.tjheslin1.westie.infrastructure.JiraIssues;

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
 * Checks that the status of Jira issues are in an accepted state for all
 * references to Jira tickets in to-do comments.
 */
public class JiraTodoMustFollowStructure extends StaticAnalysis {

    private static final String JIRA_TODO_REGEX_FORMAT = ".*//[ ]*TODO.*%s.*";

    private final JiraIssues jiraIssues;
    private final String jiraRegex;

    public JiraTodoMustFollowStructure(JiraIssues jiraIssues, String jiraRegex, List<String> javaFilesToIgnore) {
        super(javaFilesToIgnore);
        this.jiraIssues = jiraIssues;
        this.jiraRegex = jiraRegex;
    }

    /**
     * @param pathToCheck The package to check source files for to-do comments
     *                    which reference Jira tickets.
     * @return A list of violations in which to-do comments are referencing Jira issues
     * which are not in the list of accepted states, defined in {@link JiraIssues}.
     * @throws IOException if an I/O error occurs when opening the directory.
     */
    public List<Violation> checkAllJiraTodosAreInAllowedStatuses(Path pathToCheck) throws IOException {
        return Files.list(pathToCheck)
                .filter(this::isAJavaFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::checkJiraTodos)
                .peek(this::reportViolation)
                .collect(toList());
    }

    private Stream<Violation> checkJiraTodos(Path file) {
        try {
            return Files.lines(file).collect(toList()).stream()
                    .filter(this::jiraTodoLine)
                    .filter(this::jiraIssueInUnacceptedState)
                    .map(jiraTodoLine -> new Violation(file, jiraTodoLine));
        } catch (IOException e) {
            return Stream.of(new Violation(file, "Unable to read file."));
        }
    }

    private boolean jiraTodoLine(String line) {
        return line.matches(format(JIRA_TODO_REGEX_FORMAT, jiraRegex));
    }

    private boolean jiraIssueInUnacceptedState(String line) {
        Pattern pattern = Pattern.compile(jiraRegex);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String jiraIssue = matcher.group(0);
            return !jiraIssues.isJiraIssueInAllowedStatus(jiraIssue);
        } else {
            throw new IllegalStateException(format("Unable to find Jira Issue in line '%s' using regex '%s'", line, jiraRegex));
        }
    }

    private void reportViolation(Violation violation) {
        System.out.println(format("Violation!%n'%s'%nThe above violation was caused by an import which " +
                        "does not matching any of the import restrictions specified in the Westie class: '%s'",
                violation, this.getClass().getSimpleName()));
    }
}
