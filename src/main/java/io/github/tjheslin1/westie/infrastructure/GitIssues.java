package io.github.tjheslin1.westie.infrastructure;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.tjheslin1.westie.HttpClient;
import io.github.tjheslin1.westie.http.Request;
import io.github.tjheslin1.westie.http.RequestBuilder;
import io.github.tjheslin1.westie.http.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.tjheslin1.westie.WestieRegexes.EXTRACT_NUMBER_REGEX;
import static java.lang.String.format;

public class GitIssues {

    private static final String GITHUB_API_HOSTNAME = "https://api.github.com";
    private static final String GIT_ISSUE_URL_FORMAT = "/repos/%s/%s/issues/%s";
    private static final String OPEN = "open";

    private final HttpClient httpClient;
    private final String user;
    private final String repository;
    private final String githubApiHostname;

    public GitIssues(HttpClient httpClient, String user, String repository) {
        this.httpClient = httpClient;
        this.user = user;
        this.repository = repository;
        this.githubApiHostname = GITHUB_API_HOSTNAME;
    }

    public GitIssues(HttpClient httpClient, String user, String repository, String githubApiHostname) {
        this.httpClient = httpClient;
        this.user = user;
        this.repository = repository;
        this.githubApiHostname = githubApiHostname;
    }

    public boolean isGitIssueOpen(String issue) {
        String issueState = issueStatusCache.getUnchecked(extractNumber(issue));
        return issueState.equals(OPEN);
    }

    private String extractNumber(String issue) {
        Pattern pattern = Pattern.compile(EXTRACT_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(issue);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new IllegalStateException(format("Unable to find Git Issue with reference '%s'", issue));
        }
    }

    private final LoadingCache<String, String> issueStatusCache = CacheBuilder.<String, String>newBuilder()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String issueNumber) throws Exception {
                    return gitIssueStatus(issueNumber);
                }
            });

    private String gitIssueStatus(String issueNumber) throws IOException {
        Request request = new RequestBuilder().get()
                .url(format(githubApiHostname + GIT_ISSUE_URL_FORMAT, user, repository, issueNumber))
                .build();
        Response response = httpClient.execute(request);
        if (!response.isSuccessful()) {
            throw new IllegalStateException(format("Problem fetching issue:%n%s", response));
        }
        JSONObject jsonObject = new JSONObject(response.body);
        return status(jsonObject);
    }

    private String status(JSONObject jsonObject) {
        return jsonObject.getString("state");
    }
}
