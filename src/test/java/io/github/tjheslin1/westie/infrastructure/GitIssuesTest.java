package io.github.tjheslin1.westie.infrastructure;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.assertj.core.api.WithAssertions;
import org.junit.Rule;
import org.junit.Test;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.String.format;

public class GitIssuesTest implements WithAssertions {

    private static final String WIREMOCK_URL_FORMAT = "/repos/%s/%s/issues/%s";
    private static final String TEST_USER = "testUser";
    private static final String TEST_REPO = "testRepo";

    private final ApacheHttpClient httpClient = new ApacheHttpClient(Duration.ofSeconds(5));
    private final GitIssues gitIssues = new GitIssues(httpClient, TEST_USER, TEST_REPO, "http://localhost:8089");

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void requestStatusFromGitOncePerIssueNumber() throws Exception {
        String gitIssue = "100";

        UrlPattern urlPattern = urlEqualTo(format(WIREMOCK_URL_FORMAT, TEST_USER, TEST_REPO, gitIssue));
        stubFor(get(urlPattern)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\n" +
                                "  \"url\": \"https://api.github.com/repos/tjheslin1/Westie/issues/2\",\n" +
                                "  \"repository_url\": \"https://api.github.com/repos/tjheslin1/Westie\",\n" +
                                "  \"id\": 191115256,\n" +
                                "  \"number\": 2,\n" +
                                "  \"title\": \"Integrate/steal/whatever domain-enforcer\",\n" +
                                "  \"user\": {\n" +
                                "    \"login\": \"anotherUser\"\n" +
                                "  },\n" +
                                "  \"state\": \"open\",\n" +
                                "  \"body\": \"a comment\"\n" +
                                "}")));

        assertThat(gitIssues.isGitIssueOpen(gitIssue)).isTrue();
        assertThat(gitIssues.isGitIssueOpen(gitIssue)).isTrue();

        verify(1, getRequestedFor(urlPattern));
    }
}