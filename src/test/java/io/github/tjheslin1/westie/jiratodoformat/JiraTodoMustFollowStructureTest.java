package io.github.tjheslin1.westie.jiratodoformat;

import io.github.tjheslin1.westie.WithMockito;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class JiraTodoMustFollowStructureTest implements WithAssertions, WithMockito {

    public static final String ISSUE_NUMBER = "MON-100";
    private final JiraIssues jiraIssues = mock(JiraIssues.class);

    private final JiraTodoMustFollowStructure jiraTodoMustFollowStructure = new JiraTodoMustFollowStructure(jiraIssues);

    @Test
    public void enforcesJiraTicketsLinkedInTodosAreInAnAllowedState() throws Exception {
        when(jiraIssues.isJiraIssueInDevelopment(ISSUE_NUMBER));

        jiraIssues.isJiraIssueInDevelopment(ISSUE_NUMBER);
    }
}