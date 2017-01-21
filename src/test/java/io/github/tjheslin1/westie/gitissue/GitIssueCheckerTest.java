package io.github.tjheslin1.westie.gitissue;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WithMockito;
import io.github.tjheslin1.westie.infrastructure.GitIssues;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class GitIssueCheckerTest implements WithAssertions, WithMockito {

    private final GitIssues gitIssues = mock(GitIssues.class);

    @Test
    public void checksGitIssueIsInOpenState() throws Exception {
        GitIssueChecker gitIssueChecker = new GitIssueChecker(gitIssues, "Git-[0-9]{1,4}", emptyList());

        List<Violation> violations = gitIssueChecker.todosAreInOpenState(Paths.get("src/test/resources/io/github/tjheslin1/examples/git"));

        assertThat(violations).hasSize(4);
        assertThat(violations.get(0).toString()).matches("'//TODO Git-100 make this final' in file '.*/io/github/tjheslin1/examples/git/ClassWithGitIssueTodo.java.*");
        assertThat(violations.get(1).toString()).matches("'// TODO Git-101 set passed parameter as name' in file '.*/io/github/tjheslin1/examples/git/ClassWithGitIssueTodo.java.*");
        assertThat(violations.get(2).toString()).matches("'//TODO Git-100 make this final' in file '.*/io/github/tjheslin1/examples/git/nested/AnotherClassWithGitIssueTodo.java.*");
        assertThat(violations.get(3).toString()).matches("'// TODO Git-101 set passed parameter as name' in file '.*/io/github/tjheslin1/examples/git/nested/AnotherClassWithGitIssueTodo.java.*");
    }

    @Test
    public void ignoresExcludedFiles() throws Exception {
        GitIssueChecker gitIssueChecker = new GitIssueChecker(gitIssues, "Git-[0-9]{1,4}", singletonList("AnotherClassWithGitIssueTodo.java"));

        List<Violation> violations = gitIssueChecker.todosAreInOpenState(Paths.get("src/test/resources/io/github/tjheslin1/examples/git"));

        assertThat(violations).hasSize(2);
        assertThat(violations.get(0).toString()).matches("'//TODO Git-100 make this final' in file '.*/io/github/tjheslin1/examples/git/ClassWithGitIssueTodo.java.*");
        assertThat(violations.get(1).toString()).matches("'// TODO Git-101 set passed parameter as name' in file '.*/io/github/tjheslin1/examples/git/ClassWithGitIssueTodo.java.*");
    }
}