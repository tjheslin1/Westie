package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.infrastructure.ApacheHttpClient;
import io.github.tjheslin1.westie.infrastructure.JiraIssues;
import io.github.tjheslin1.westie.jiratodoformat.JiraTodoMustFollowStructure;
import org.assertj.core.api.WithAssertions;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class WestieTest implements WithAssertions {

    public static final Duration MAX_IDLE_TIME = Duration.ofSeconds(5);
    private static final String JIRA_URL_FORMAT = "";
    private static final String JIRA_USERNAME = "";
    private static final String JIRA_PASSWORD = "";
    private static final Path BASE_PACKAGE = Paths.get("test/java/io/github/tjheslin1/westie");
    public static final ApacheHttpClient HTTP_CLIENT = new ApacheHttpClient(MAX_IDLE_TIME);

    // Example displayed in README.md
    @Ignore
    @Test
    public void canOnlyReferenceJiraIssuesInDevelopment() throws Exception {
        JiraIssues jiraIssues = new JiraIssues(HTTP_CLIENT, JIRA_URL_FORMAT, JIRA_USERNAME, JIRA_PASSWORD, singletonList("Development"));
        JiraTodoMustFollowStructure jiraTodoMustFollowStructure = new JiraTodoMustFollowStructure(jiraIssues, "JIRA-[0-9]{3}", emptyList());

        List<Violation> violations = jiraTodoMustFollowStructure.checkAllJiraTodosAreInAllowedStatuses(BASE_PACKAGE);

        assertThat(violations).isEmpty();
    }
}