package io.github.tjheslin1.westie.infrastructure;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.assertj.core.api.WithAssertions;
import org.junit.Rule;
import org.junit.Test;

import java.time.Duration;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;

public class JiraIssuesTest implements WithAssertions {
    public static final List<String> ALLOWED_STATUSES = asList("Ready To Play", "Development");
    public static final String WIREMOCK_URL_FORMAT = "/rest/api/2/issue/%s?&os_username=%s&os_password=%s";
    public static final String JIRA_URL_FORMAT = "http://localhost:8089" + WIREMOCK_URL_FORMAT;
    public static final String TEST_USER = "testUser";
    public static final String TEST_PASS = "testPass";

    private final ApacheHttpClient httpClient = new ApacheHttpClient(Duration.ofSeconds(5));
    private final JiraIssues jiraIssues = new JiraIssues(httpClient, JIRA_URL_FORMAT, TEST_USER, TEST_PASS, ALLOWED_STATUSES);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void requestStatusFromJiraOncePerIssueNumber() throws Exception {
        String issue = "MON-100";

        UrlPattern urlPattern = urlEqualTo(format(WIREMOCK_URL_FORMAT, issue, TEST_USER, TEST_PASS));
        stubFor(get(urlPattern)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"fields\": {\"status\": {\"name\": \"Development\"}}}")));

        assertThat(jiraIssues.isJiraIssueInAllowedStatus(issue)).isTrue();
        assertThat(jiraIssues.isJiraIssueInAllowedStatus(issue)).isTrue();

        verify(1, getRequestedFor(urlPattern));
    }
}