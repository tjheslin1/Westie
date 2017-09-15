/*
 * Copyright 2017 Thomas Heslin <tjheslin1@gmail.com>.
 *
 * This file is part of Westie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.tjheslin1.westie.WestieRegexes.EXTRACT_NUMBER_REGEX;
import static java.lang.String.format;

/**
 * Retrieves the state of a given Git issue.
 * Checking the issue is in the open state.
 */
public class GitIssues {

    private static final String GITHUB_API_HOSTNAME = "https://api.github.com";
    private static final String GIT_ISSUE_URL_FORMAT = "/repos/%s/%s/issues/%s";
    private static final String OPEN = "open";
    private static final HttpClient HTTP_CLIENT = new ApacheHttpClient(Duration.ofSeconds(10));

    private final HttpClient httpClient;
    private final String user;
    private final String repository;
    private final String githubApiHostname;

    public GitIssues(String user, String repository) {
        this.httpClient = HTTP_CLIENT;
        this.user = user;
        this.repository = repository;
        this.githubApiHostname = GITHUB_API_HOSTNAME;
    }

    public GitIssues(String user, String repository, HttpClient httpClient) {
        this.httpClient = httpClient;
        this.user = user;
        this.repository = repository;
        this.githubApiHostname = GITHUB_API_HOSTNAME;
    }

    public GitIssues(String user, String repository, String githubApiHostname, HttpClient httpClient) {
        this.httpClient = httpClient;
        this.user = user;
        this.repository = repository;
        this.githubApiHostname = githubApiHostname;
    }

    /**
     * @param issue The git repo issue to query the github API to determine its state.
     * @return true if the state of the issue is 'open'. false otherwise.
     */
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
