/*
 * Copyright 2016 Thomas Heslin <tjheslin1@gmail.com>.
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
import java.util.List;

import static java.lang.String.format;

/**
 * Retrieves the status information of a given Jira issue.
 * <p>
 * Contribution by @theangrydev:
 * - Uses caching so that multiple references to the same Jira
 * issue does result in retrieving the information from your Jira
 * site more than once.
 */
public class JiraIssues {

    private static final String JIRA_TICKET_QUERY_FORMAT = "/rest/api/2/issue/%s?&os_username=%s&os_password=%s";

    private final HttpClient httpClient;
    private final String teamCityUsername;
    private final String teamCityPassword;
    private final String jiraHostname;
    private List<String> allowedStatuses;

    /**
     * @param httpClient       Your chosen http client. Provided is {@link ApacheHttpClient}.
     * @param jiraHostname     Your jira's hostname (e.g. "tasktracker.mycompany.com").
     * @param teamCityUsername Your username to access your Jira.
     * @param teamCityPassword Your password to access your Jira.
     * @param allowedStatuses  A list of allowed status (e.g [Ready To Play, Development]).
     */
    public JiraIssues(HttpClient httpClient, String jiraHostname, String teamCityUsername, String teamCityPassword, List<String> allowedStatuses) {
        this.httpClient = httpClient;
        this.teamCityUsername = teamCityUsername;
        this.teamCityPassword = teamCityPassword;
        this.jiraHostname = jiraHostname;
        this.allowedStatuses = allowedStatuses;
    }

    /**
     * @param issueNumber The issue number of the Jira issue. (e.g TOM-100)
     * @return true if the Jira issue is in one of the 'allowedStatuses'.
     */
    public boolean isJiraIssueInAllowedStatus(String issueNumber) {
        String issueStatus = issueStatusCache.getUnchecked(issueNumber);
        return allowedStatuses.stream().anyMatch(allowedStatus -> allowedStatus.equalsIgnoreCase(issueStatus));
    }

    /**
     * @return The list of provided accepted status' for a Jira story to be in and be referenced in a to-do.
     */
    public List<String> allowedStatuses() {
        return allowedStatuses;
    }

    private final LoadingCache<String, String> issueStatusCache = CacheBuilder.<String, String>newBuilder()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String issueNumber) throws Exception {
                    return jiraIssueStatus(issueNumber);
                }
            });

    private String jiraIssueStatus(String issueNumber) throws IOException {
        Response response = jiraIssue(issueNumber);
        if (!response.isSuccessful()) {
            throw new IllegalStateException(format("Problem fetching issue:%n%s", response));
        }
        JSONObject jsonObject = new JSONObject(response.body);
        return status(jsonObject);
    }

    private Response jiraIssue(String issueNumber) throws IOException {
        Request request = new RequestBuilder().get()
                .url(format(jiraHostname + JIRA_TICKET_QUERY_FORMAT,
                        issueNumber, teamCityUsername, teamCityPassword))
                .build();
        return httpClient.execute(request);
    }

    private String status(JSONObject jsonObject) {
        return jsonObject.getJSONObject("fields").getJSONObject("status").getString("name");
    }
}
