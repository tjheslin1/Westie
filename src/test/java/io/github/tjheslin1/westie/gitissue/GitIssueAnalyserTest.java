package io.github.tjheslin1.westie.gitissue;

import io.github.tjheslin1.westie.LineAssertions;
import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WithMockito;
import io.github.tjheslin1.westie.infrastructure.GitIssues;
import io.github.tjheslin1.westie.testinfrastructure.TestWestieFileReader;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.singletonList;

public class GitIssueAnalyserTest implements WithAssertions, WithMockito {

    private final GitIssues gitIssues = mock(GitIssues.class);

    @Test
    public void checksGitIssueIsInOpenState() throws Exception {
        GitIssueAnalyser gitIssueAnalyser = new GitIssueAnalyser(gitIssues, "Git-[0-9]{1,4}", new TestWestieFileReader());

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/git");
        List<Violation> violations = gitIssueAnalyser.todosAreInOpenState(pathToCheck);

        assertThat(violations).hasSize(4);
        LineAssertions lineMatchAssertions = new LineAssertions(violations);
        lineMatchAssertions.containsViolationMessage("Violation in file 'ClassWithGitIssueTodo.java'\n" +
                "\n" +
                "    //TODO Git-100 make this final\n" +
                "\n" +
                "Violation was caused by a reference to a Git issue which is not in the open state.\n");
        lineMatchAssertions.containsViolationMessage("Violation in file 'ClassWithGitIssueTodo.java'\n" +
                "\n" +
                "        // TODO Git-101 set passed parameter as name\n" +
                "\n" +
                "Violation was caused by a reference to a Git issue which is not in the open state.\n");
        lineMatchAssertions.containsViolationMessage("Violation in file 'AnotherClassWithGitIssueTodo.java'\n" +
                "\n" +
                "    //TODO Git-100 make this final\n" +
                "\n" +
                "Violation was caused by a reference to a Git issue which is not in the open state.\n");
        lineMatchAssertions.containsViolationMessage("Violation in file 'AnotherClassWithGitIssueTodo.java'\n" +
                "\n" +
                "        // TODO Git-101 set passed parameter as name\n" +
                "\n" +
                "Violation was caused by a reference to a Git issue which is not in the open state.\n");
    }

    @Test
    public void ignoresExcludedFiles() throws Exception {
        GitIssueAnalyser gitIssueAnalyser = new GitIssueAnalyser(gitIssues, "Git-[0-9]{1,4}", new TestWestieFileReader());

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/git");
        List<Violation> violations = gitIssueAnalyser.todosAreInOpenState(pathToCheck, singletonList("AnotherClassWithGitIssueTodo.java"));

        assertThat(violations).hasSize(2);
        LineAssertions lineMatchAssertions = new LineAssertions(violations);
        lineMatchAssertions.containsViolationMessage("Violation in file 'ClassWithGitIssueTodo.java'\n" +
                "\n" +
                "    //TODO Git-100 make this final\n" +
                "\n" +
                "Violation was caused by a reference to a Git issue which is not in the open state.\n");
        lineMatchAssertions.containsViolationMessage("Violation in file 'ClassWithGitIssueTodo.java'\n" +
                "\n" +
                "        // TODO Git-101 set passed parameter as name\n" +
                "\n" +
                "Violation was caused by a reference to a Git issue which is not in the open state.\n");
    }
}