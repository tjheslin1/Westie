package io.github.tjheslin1.westie.jiraissue;

import io.github.tjheslin1.westie.FileLineViolation;
import io.github.tjheslin1.westie.LineAssertions;
import io.github.tjheslin1.westie.FileViolation;
import io.github.tjheslin1.westie.WithMockito;
import io.github.tjheslin1.westie.infrastructure.JiraIssues;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class JiraReferenceCheckerTest implements WithAssertions, WithMockito {

    private static final String JIRA_ISSUE_REGEX = "MON-[0-9]{3}";
    private static final String ISSUE_NUMBER = "MON-100";

    private final JiraIssues jiraIssues = mock(JiraIssues.class);

    @Test
    public void findsJiraIssueWhichIsInDevComplete() throws Exception {
        when(jiraIssues.isJiraIssueInAllowedStatus(ISSUE_NUMBER)).thenReturn(false);
        when(jiraIssues.allowedStatuses()).thenReturn(asList("Ready To Play", "Development"));

        JiraReferenceChecker jiraReferenceChecker = new JiraReferenceChecker(jiraIssues, JIRA_ISSUE_REGEX);

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/jira");
        List<FileLineViolation> violations = jiraReferenceChecker.todosAreInAllowedStatuses(pathToCheck);

        assertThat(violations.size()).isEqualTo(3);
        LineAssertions lineAssertions = new LineAssertions(violations);
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithJiraTodos.java'\n" +
                "\n" +
                "    //TODO MON-100 make this final\n" +
                "\n" +
                "Violation was caused by a reference to a Jira issue which is not in any of the accepted statuses: '[Ready To Play, Development]'.\n");
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithJiraTodos.java'\n" +
                "\n" +
                "        // TODO MON-101 set passed parameter as name\n" +
                "\n" +
                "Violation was caused by a reference to a Jira issue which is not in any of the accepted statuses: '[Ready To Play, Development]'.\n");
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithJiraTodos.java'\n" +
                "\n" +
                "    // todo MON-101 blah\n" +
                "\n" +
                "Violation was caused by a reference to a Jira issue which is not in any of the accepted statuses: '[Ready To Play, Development]'.\n");
    }

    @Test
    public void doesNotReportOnIgnoredFiles() throws Exception {
        when(jiraIssues.isJiraIssueInAllowedStatus(ISSUE_NUMBER)).thenReturn(false);
        when(jiraIssues.allowedStatuses()).thenReturn(asList("Ready To Play", "Development"));

        JiraReferenceChecker jiraReferenceChecker = new JiraReferenceChecker(jiraIssues, JIRA_ISSUE_REGEX, singletonList("ClassWithJiraTodos.java"));

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/jira");
        List<FileLineViolation> violations = jiraReferenceChecker.todosAreInAllowedStatuses(pathToCheck);

        assertThat(violations).isEmpty();
    }
}