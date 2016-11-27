package io.github.tjheslin1.westie.gitissue;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WithMockito;
import io.github.tjheslin1.westie.infrastructure.GitIssues;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.emptyList;

public class GitIssueCheckerTest implements WithAssertions, WithMockito {

    private final GitIssues gitIssues = mock(GitIssues.class);

    @Test
    public void checksGitIssueIsInOpenState() throws Exception {
        GitIssueChecker gitIssueChecker = new GitIssueChecker(gitIssues, "Git-[0-9]{1,3}", emptyList());

        List<Violation> violations = gitIssueChecker.todosAreInOpenState(Paths.get("src/test/resources/io/github/tjheslin1/examples/io.github.tjheslin1.examples.git"));

        assertThat(violations).isEmpty();
    }
}