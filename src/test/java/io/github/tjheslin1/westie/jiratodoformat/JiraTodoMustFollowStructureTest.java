package io.github.tjheslin1.westie.jiratodoformat;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WithMockito;
import io.github.tjheslin1.westie.infrastructure.JiraIssues;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class JiraTodoMustFollowStructureTest implements WithAssertions, WithMockito {

    private static final String JIRA_ISSUE_REGEX = "MON-[0-9]{3}";
    private static final String ISSUE_NUMBER = "MON-100";

    private final JiraIssues jiraIssues = mock(JiraIssues.class);

    @Test
    public void findsTodoForAJiraStoryWhichIsInDevComplete() throws Exception {
        when(jiraIssues.isJiraIssueInAllowedStatus(ISSUE_NUMBER)).thenReturn(false);
        when(jiraIssues.allowedStatuses()).thenReturn(asList("Ready To Play", "Development"));

        JiraTodoMustFollowStructure jiraTodoMustFollowStructure = new JiraTodoMustFollowStructure(jiraIssues, JIRA_ISSUE_REGEX, emptyList());

        Path testFilePath = Paths.get(JiraTodoMustFollowStructure.class.getClassLoader().getResource("io/github/tjheslin1/examples/jira/ClassWithJiraTodos.java").toURI());
        List<Violation> violations = jiraTodoMustFollowStructure.checkAllJiraTodosAreInAllowedStatuses(testFilePath.getParent());

        assertThat(violations.size()).isEqualTo(2);
        assertThat(violations.get(0).toString()).matches("Line '//TODO MON-100 make this final' in file '.*/io/github/tjheslin1/examples/jira/ClassWithJiraTodos.java.*");
        assertThat(violations.get(1).toString()).matches("Line '// TODO MON-101 set passed parameter as name' in file '.*/io/github/tjheslin1/examples/jira/ClassWithJiraTodos.java.*");
    }
}