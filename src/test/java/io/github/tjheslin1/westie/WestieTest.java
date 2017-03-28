package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.environmentproperties.EnvironmentPropertiesAnalyser;
import io.github.tjheslin1.westie.infrastructure.ApacheHttpClient;
import io.github.tjheslin1.westie.infrastructure.JiraIssues;
import io.github.tjheslin1.westie.jiraissue.JiraReferenceAnalyser;
import org.assertj.core.api.WithAssertions;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import static java.util.Collections.singletonList;

public class WestieTest implements WithAssertions {

    private static final Duration MAX_IDLE_TIME = Duration.ofSeconds(5);
    private static final String JIRA_HOSTNAME = "";
    private static final String JIRA_USERNAME = "";
    private static final String JIRA_PASSWORD = "";
    private static final Path BASE_PACKAGE = Paths.get("test/java/io/github/tjheslin1/westie");
    private static final ApacheHttpClient HTTP_CLIENT = new ApacheHttpClient(MAX_IDLE_TIME);

    private static final Path PROPERTIES_DIR = null;

    // Example displayed in README.md
    @Ignore
    @Test
    public void allEnvironmentPropertiesFilesHaveTheSameKeys() throws Exception {
        EnvironmentPropertiesAnalyser analyser = new EnvironmentPropertiesAnalyser();

        List<FileViolation> violations = analyser.propertiesProvidedForAllEnvironments(PROPERTIES_DIR);

        assertThat(violations).isEmpty();
    }

    // Example displayed in README.md
    @Ignore
    @Test
    public void canOnlyReferenceJiraIssuesInDevelopment() throws Exception {
        JiraIssues jiraIssues = new JiraIssues(HTTP_CLIENT, JIRA_HOSTNAME, JIRA_USERNAME, JIRA_PASSWORD, singletonList("Development"));
        JiraReferenceAnalyser jiraReferenceAnalyser = new JiraReferenceAnalyser(jiraIssues, "JIRA-[0-9]{3}");

        List<FileLineViolation> violations = jiraReferenceAnalyser.todosAreInAllowedStatuses(BASE_PACKAGE);

        assertThat(violations).isEmpty();
    }
}