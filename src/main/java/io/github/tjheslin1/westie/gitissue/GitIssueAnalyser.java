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
package io.github.tjheslin1.westie.gitissue;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WestieAnalyser;
import io.github.tjheslin1.westie.infrastructure.GitIssues;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

/**
 * Checks that all Git issues referenced in to-do comments are in the
 * open state.
 */
public class GitIssueAnalyser {

    private static final String GIT_TODO_REGEX_FORMAT = ".*//.*(T|t)(O|o)(D|d)(O|o).*%s.*";

    private final GitIssues gitIssues;
    private final String gitRegex;
    private final WestieAnalyser westieAnalyser;

    public GitIssueAnalyser(GitIssues gitIssues, String gitRegex) {
        this.gitIssues = gitIssues;
        this.gitRegex = gitRegex;
        this.westieAnalyser = new WestieAnalyser();
    }

    public GitIssueAnalyser(GitIssues gitIssues, String gitRegex, WestieFileReader fileReader) {
        this.gitIssues = gitIssues;
        this.gitRegex = gitRegex;
        this.westieAnalyser = new WestieAnalyser(fileReader);
    }

    /**
     * @param pathToCheck The package to check source files for to-do comments
     *                    which reference Git issues.
     * @return A list of violations in which to-do comments are referencing Git issues
     * which are not in the open state.
     * @throws IOException if an I/O error occurs when opening the directory.
     */

    public List<Violation> todosAreInOpenState(Path pathToCheck) throws IOException {
        return todosAreInOpenState(pathToCheck, emptyList());
    }

    /**
     * @param pathToCheck   The package to check source files for to-do comments
     *                      which reference Git issues.
     * @param filesToIgnore files exempt from analysis.
     * @return A list of violations in which to-do comments are referencing Git issues
     * which are not in the open state.
     * @throws IOException if an I/O error occurs when opening the directory.
     */
    public List<Violation> todosAreInOpenState(Path pathToCheck, List<String> filesToIgnore) throws IOException {
        return westieAnalyser.analyseDirectory(pathToCheck)
                .forAllFiles().ignoring(filesToIgnore)
                .analyseLinesOfFile(this::checkGitIssues, "Violation was caused by a reference to a " +
                        "Git issue which is not in the open state.");
    }

    private boolean checkGitIssues(String line) {
        return isGitTodoLine(line) && gitIssueIsInNotInTheOpenState(line);
    }

    private boolean isGitTodoLine(String line) {
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
}
