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
package io.github.tjheslin1.westie.jiratodoformat;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.tjheslin1.westie.HttpClient;
import io.github.tjheslin1.westie.Response;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.json.JSONObject;

import java.util.List;

import static java.lang.String.format;

public class JiraIssues {

    private final HttpClient httpClient;
    private final String teamCityUsername;
    private final String teamCityPassword;
    private List<String> allowedStatuses;

    public JiraIssues(HttpClient httpClient, String teamCityUsername, String teamCityPassword, List<String> allowedStatuses) {
        this.httpClient = httpClient;
        this.teamCityUsername = teamCityUsername;
        this.teamCityPassword = teamCityPassword;
        this.allowedStatuses = allowedStatuses;
    }

    public boolean isJiraIssueInAllowedStatus(String issueNumber) {
        String issueStatus = issueStatusCache.getUnchecked(issueNumber);
        return allowedStatuses.stream().anyMatch(allowedStatus -> allowedStatus.equalsIgnoreCase(issueStatus));
    }

    private final LoadingCache<String, String> issueStatusCache = CacheBuilder.<String, String>newBuilder()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String issueNumber) throws Exception {
                    return jiraIssueStatus(issueNumber);
                }
            });

    private String jiraIssueStatus(String issueNumber) {
        Response response = jiraIssue(issueNumber);
        if (!response.isSuccessful()) {
            throw new IllegalStateException(format("Problem fetching issue:%n%s", response));
        }
        JSONObject jsonObject = new JSONObject(response.body);
        return status(jsonObject);
    }

    private Response jiraIssue(String issueNumber) {
        HttpUriRequest apacheRequest = RequestBuilder.get()
                .setUri("https://tasktracker.sns.sky.com/rest/api/2/issue/" + issueNumber + "?&os_username=" + teamCityUsername + "&os_password=" + teamCityPassword)
                .build();
        return (Response) httpClient.execute(apacheRequest);
    }

    private String status(JSONObject jsonObject) {
        return jsonObject.getJSONObject("fields").getJSONObject("status").getString("name");
    }
}
