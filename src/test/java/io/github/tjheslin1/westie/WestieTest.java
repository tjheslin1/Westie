package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.importrestrictions.ImportRestriction;
import io.github.tjheslin1.westie.importrestrictions.ImportsRestrictionChecker;
import io.github.tjheslin1.westie.infrastructure.ApacheHttpClient;
import io.github.tjheslin1.westie.infrastructure.JiraIssues;
import io.github.tjheslin1.westie.jiraallowedstatus.JiraReferenceChecker;
import org.assertj.core.api.WithAssertions;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import static io.github.tjheslin1.westie.importrestrictions.ImportRestriction.importRestriction;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class WestieTest implements WithAssertions {

    public static final Duration MAX_IDLE_TIME = Duration.ofSeconds(5);
    private static final String JIRA_URL_FORMAT = "";
    private static final String JIRA_USERNAME = "";
    private static final String JIRA_PASSWORD = "";
    private static final Path BASE_PACKAGE = Paths.get("test/java/io/github/tjheslin1/westie");
    public static final ApacheHttpClient HTTP_CLIENT = new ApacheHttpClient(MAX_IDLE_TIME);

    private static final List<String> FILES_TO_IGNORE = emptyList();

    // Example displayed in README.md
    @Ignore
    @Test
    public void oracleImportsConfinedToDatabasePackage() throws Exception {
        ImportRestriction oracleImportRestriction = importRestriction("io.github.tjheslin1.database", "import oracle.jdbc.*");
        ImportsRestrictionChecker importCheck = new ImportsRestrictionChecker(singletonList(oracleImportRestriction), FILES_TO_IGNORE);

        List<Violation> violations = importCheck.checkImportsAreOnlyUsedInAcceptedPackages(BASE_PACKAGE);

        assertThat(violations).isEmpty();
    }

    // Example displayed in README.md
    @Ignore
    @Test
    public void canOnlyReferenceJiraIssuesInDevelopment() throws Exception {
        JiraIssues jiraIssues = new JiraIssues(HTTP_CLIENT, JIRA_URL_FORMAT, JIRA_USERNAME, JIRA_PASSWORD, singletonList("Development"));
        JiraReferenceChecker jiraReferenceChecker = new JiraReferenceChecker(jiraIssues, "JIRA-[0-9]{3}", emptyList());

        List<Violation> violations = jiraReferenceChecker.checkAllJiraTodosAreInAllowedStatuses(BASE_PACKAGE);

        assertThat(violations).isEmpty();
    }
}