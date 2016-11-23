package io.github.tjheslin1.westie.jiraallowedstatus;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WithMockito;
import io.github.tjheslin1.westie.infrastructure.JiraIssues;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;

public class JiraReferenceCheckerTest implements WithAssertions, WithMockito {

    private static final String JIRA_ISSUE_REGEX = "MON-[0-9]{3}";
    private static final String ISSUE_NUMBER = "MON-100";

    private final JiraIssues jiraIssues = mock(JiraIssues.class);

    @Test
    public void findsTodoForAJiraStoryWhichIsInDevComplete() throws Exception {
        when(jiraIssues.isJiraIssueInAllowedStatus(ISSUE_NUMBER)).thenReturn(false);
        when(jiraIssues.allowedStatuses()).thenReturn(asList("Ready To Play", "Development"));

        JiraReferenceChecker jiraReferenceChecker = new JiraReferenceChecker(jiraIssues, JIRA_ISSUE_REGEX);

        List<Violation> violations = jiraReferenceChecker.checkAllJiraTodosAreInAllowedStatuses(Paths.get("src/test/resources/io/github/tjheslin1/examples/jira"));

        assertThat(violations.size()).isEqualTo(2);
        assertThat(violations.get(0).toString()).matches("Line '//TODO MON-100 make this final' in file '.*/io/github/tjheslin1/examples/jira/many/packages/ClassWithJiraTodos.java.*");
        assertThat(violations.get(1).toString()).matches("Line '// TODO MON-101 set passed parameter as name' in file '.*/io/github/tjheslin1/examples/jira/many/packages/ClassWithJiraTodos.java.*");
    }
}