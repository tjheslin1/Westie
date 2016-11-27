package io.github.tjheslin1.westie.gitissue;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WestieChecker;
import io.github.tjheslin1.westie.infrastructure.GitIssues;

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
