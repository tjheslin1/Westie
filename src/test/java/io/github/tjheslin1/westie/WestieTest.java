package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.environmentproperties.EnvironmentPropertiesAnalyser;
import io.github.tjheslin1.westie.infrastructure.ApacheHttpClient;
import io.github.tjheslin1.westie.infrastructure.JiraIssues;
import io.github.tjheslin1.westie.jiraissue.JiraReferenceAnalyser;
import org.assertj.core.api.WithAssertions;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class WestieTest implements WithAssertions {

    private static final String JIRA_STORY_REGEX = "JIRA-[0-9]{1,4}";
    private static final Duration MAX_IDLE_TIME = Duration.ofSeconds(5);
    private static final String JIRA_HOSTNAME = "";
    private static final String JIRA_USERNAME = "";
    private static final String JIRA_PASSWORD = "";
    private static final Path BASE_PACKAGE = Paths.get("test/java/io/github/tjheslin1/westie");

    private static final ApacheHttpClient HTTP_CLIENT = new ApacheHttpClient(MAX_IDLE_TIME);
    private static final Path PROPERTIES_DIR = null;
    private static final List<String> FILES_TO_IGNORE = emptyList();

    private final JiraIssues jiraIssues = new JiraIssues(HTTP_CLIENT, JIRA_HOSTNAME, JIRA_USERNAME, JIRA_PASSWORD, singletonList("Development"));

    // Example displayed in README.md
    @Ignore
    @Test
    public void allEnvironmentPropertiesFilesHaveTheSameKeys() throws Exception {
        List<FileViolation> violations = new EnvironmentPropertiesAnalyser()
                .propertiesProvidedForAllEnvironments(PROPERTIES_DIR);

        assertThat(violations).isEmpty();
    }

    // Example displayed in README.md
    @Ignore
    @Test
    public void canOnlyReferenceJiraIssuesInDevelopment() throws Exception {
        List<Violation> violations = new JiraReferenceAnalyser(jiraIssues, JIRA_STORY_REGEX)
                .todosAreInAllowedStatuses(BASE_PACKAGE);

        assertThat(violations).isEmpty();
    }

    // below is part of 'JiraReferenceAnalyser.java'
//    private List<Violation> todosAreInAllowedStatuses(Path pathToCheck, List<String> filesToIgnore) throws IOException {
//        return new WestieAnalyser().analyseDirectory(pathToCheck)
//                .forJavaFiles().ignoring(filesToIgnore)
//                .analyseLinesOfFile(this::checkJiraTodos, format("Violation was caused by a reference to a " +
//                        "Jira issue which is not in any of the accepted statuses: '%s'.", jiraIssues.allowedStatuses()));
//    }
//
//    private boolean checkJiraTodos(String fileLine) {
//        return isJiraTodoLine(fileLine) && jiraIssueInUnacceptedState(fileLine);
//    }
//
//    ...
}